using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace DressExchangeServer
{
    class Program
    {
        private static byte[] _buffer = new byte[1048576];
        private static List<Socket> _clientSockets = new List<Socket>();
        private static Socket _serverSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        private static Dress_DB db = new Dress_DB();

        static void Main(string[] args)
        {
            Console.Title = "Server";

            setUpServer();
            Console.ReadLine();
        }

        private static void setUpServer()
        {
            Console.WriteLine("Connecting to Database...");

            IPAddress[] ips = Dns.GetHostEntry(Dns.GetHostName()).AddressList;

            Console.WriteLine("IP to listen on:");
            int count = 0;
            foreach (IPAddress ip in ips)
                Console.WriteLine("{0}: {1}", ++count, ip.ToString());

            string numString = Console.ReadLine();
            int pos = Convert.ToInt32(numString) - 1;
            IPAddress myAddress = ips[pos]; // Removing or not the scope ID doesn't change anything as "localEndPoint" below will contain it no matter what

            //IPAddress serverIP = IPAddress.Parse("192.168.1.64");
            //IPAddress localAddr = IPAddress.Parse("0.0.0.0");
            IPEndPoint localEndPoint = new IPEndPoint(myAddress, 9876);

            Console.WriteLine("Setting up server...");
            _serverSocket.Bind(localEndPoint);

            _serverSocket.ReceiveBufferSize = 1048576;

            _serverSocket.Listen(5);
            _serverSocket.BeginAccept(new AsyncCallback(AcceptCallback), null);

        }

        private static void AcceptCallback(IAsyncResult AR)
        {
            Socket socket = _serverSocket.EndAccept(AR);

            socket.ReceiveBufferSize = 1048576;

            _clientSockets.Add(socket);
            Console.WriteLine("Client Connected...");
            socket.BeginReceive(_buffer, 0, _buffer.Length, SocketFlags.None, new AsyncCallback(ReceiveCallback), socket);
            _serverSocket.BeginAccept(new AsyncCallback(AcceptCallback), null);

        }

        private static async void ReceiveCallback(IAsyncResult AR)
        {
            try
            {
                Socket socket = (Socket)AR.AsyncState;
                socket.ReceiveBufferSize = 1048576;


                int received = socket.EndReceive(AR);

                byte[] dataBuf = new byte[received];
                Array.Copy(_buffer, dataBuf, received);

                string text = Encoding.ASCII.GetString(dataBuf);
                Console.WriteLine(text);
                text = new string(text.Where(c => !char.IsControl(c)).ToArray());

                Console.WriteLine("Text Received: " + text);

                string response = string.Empty;

                var request = text.Split(';');
                Console.WriteLine("Start");
                Console.WriteLine(request[0]);
                Console.WriteLine("End");
                if (request[0].Equals("login"))
                {
                    Console.WriteLine("Attempting to login user...");

                    response = "0;0;0";

                    string username = request[1];
                    string password = request[2];

                    var login = (from user in db.users
                                 where user.facebook_login == username
                                 select user.facebook_login).ToList();

                    foreach (var loginname in login)
                    {
                        //do something with the properties
                        Console.WriteLine("User " + loginname + " has been logged in.");

                        response = loginInformation(loginname);

                        //response += retrieveDresses();
                    }


                }
                else if (request[0].Equals("signUp"))
                {
                    Console.WriteLine("Signing up new user....");

                    string facebookID = request[1];
                    string email = request[2];
                    string fullname = request[3];

                    signup(facebookID, email, fullname);
                    response += userID(facebookID);
                }
                else if (request[0].Equals("refresh"))
                {
                    Console.WriteLine("Refreshing list of dresses");
                    int userID = int.Parse(request[1]);
                    response = retrieveDresses(userID);
                }
                else if (request[0].Equals("add"))
                {
                    Console.WriteLine("Adding new dress");

                    int user_id = int.Parse(request[1]);
                    int size = int.Parse(request[2]);
                    string title = request[3];
                    string condition = request[4];

                    //string photo_byte = request[4];
                    response += addDresses(user_id, size, title, condition);
                    //response += addDresses(user_id, size, dress_desc, photo_byte);
                }
                else if (request[0].Equals("likes"))
                {
                    Console.WriteLine("Liking...");

                    int user = int.Parse(request[1]);
                    int liked = int.Parse(request[2]);
                    response += likeAction(user, liked);
                }
                else if (request[0].Equals("liked"))
                {
                    Console.WriteLine("Retrieving list of users who have liked you...");

                    int user = int.Parse(request[1]);
                    response = liked(user);
                    //response = "HITHISISTESTLOL";
                }
                else if (request[0].Equals("pic"))
                {
                    response = pic();

                }
                else if (request[0].Equals("mine"))
                {
                    int user_id = int.Parse(request[1]);
                    response = mine(user_id);
                }

                Console.WriteLine("sending.... " + response);
                byte[] data = Encoding.ASCII.GetBytes(response);
                socket.BeginSend(data, 0, data.Length, SocketFlags.None, new AsyncCallback(SendCallback), socket);

                //socket.BeginReceive(_buffer, 0, _buffer.Length, SocketFlags.None, new AsyncCallback(ReceiveCallback), socket);
            }
            catch (SocketException e)
            {
                Console.WriteLine("Client Disconnected");
            }
        }

        private static void SendCallback(IAsyncResult AR)
        {
            Socket socket = (Socket)AR.AsyncState;
            socket.EndSend(AR);
            socket.Close();
            Console.WriteLine("Client Disconnected");
        }

        private static void signup(string facebookID, string email, string name)
        {
            var result = (from user in db.users
                          where user.facebook_login == facebookID
                          select user.users_id + ";" + user.size + ";" + user.facebook_login).ToList();

            if (result.Count == 0)
            {
                string query = "insert into users(facebook_login, email, fullname) values ('" + facebookID + "', '" + email + "', '" + name + "')";
                int noOfRowInserted = db.Database.ExecuteSqlCommand(query);
                Console.WriteLine("Inserted users: " + noOfRowInserted);
            }

        }

        private static string userID(string facebookID)
        {
            string userID = "";

            var result = (from user in db.users
                          where user.facebook_login == facebookID
                          select user.users_id).ToList();

            foreach (var identifier in result)
            {
                userID = identifier  + "";
            }

            return userID;
        }

        private static string loginInformation(string loginname)
        {
            string response = "Incorrect";
            Console.WriteLine("Start");
            var result = (from user in db.users
                          where user.facebook_login == loginname
                          select user.users_id + ";" + user.size + ";" + user.facebook_login).ToList();
            Console.WriteLine(result);
            foreach (var user in result)
            {
                //do something with the properties
                response = user + "\n";
                Console.WriteLine("here" + response);
            }

            return response;
        }

        private static string pic() {
            string response = string.Empty;

            string text = System.IO.File.ReadAllText(@"D:\Projects\Dress_Exchange\pics\text.txt");

            return text;
        }

        private static string retrieveDresses(int userID)
        {
            string response = string.Empty;

            var result = (from dress in db.dresses
                          join user in db.users on dress.users_id equals user.users_id
                          join suburb in db.suburbs on user.suburb_id equals suburb.suburb_id
                          select user.users_id + ";" + dress.dress_id + ";" + dress.size + ";" + dress.title + ";" + dress.condition + ";" + suburb.suburb_name).ToList();

            ArrayList dressList = new ArrayList();
            int counter = 0;
            foreach (var dress in result)
            {
                //do something with the properties
                var items = dress.Split(';');
                Refresh item = new Refresh();
                item.Seller_id = items[0];
                item.Dress_id = items[1];
                item.Size = items[2];
                item.title = items[3];
                item.condition = items[4];
                item.suburb = items[5];
                dressList.Add(item);

                //response += dress + "\n";
            }


            string likes = liked(userID);

            Combined combo = new Combined();
            combo.Dresses = dressList;
            combo.Likes = likes;

            response = JsonConvert.SerializeObject(combo);
            Console.WriteLine(response);
            return response;
        }

        private static string mine(int user_id)
        {
            string response = string.Empty;

            var result = (from dress in db.dresses
                          join user in db.users on dress.users_id equals user.users_id
                          join suburb in db.suburbs on user.suburb_id equals suburb.suburb_id
                          where user.users_id == user_id
                          select user.users_id + ";" + dress.dress_id + ";" + dress.size + ";" + dress.title + ";" + dress.condition + ";" + suburb.suburb_name).ToList();

            ArrayList dressList = new ArrayList();
            int counter = 0;
            foreach (var dress in result)
            {
                //do something with the properties
                var items = dress.Split(';');
                Refresh item = new Refresh();
                item.Seller_id = items[0];
                item.Dress_id = items[1];
                item.Size = items[2];
                item.title = items[3];
                item.condition = items[4];
                item.suburb = items[5];
                dressList.Add(item);

                //response += dress + "\n";
            }

            Combined combo = new Combined();
            combo.Dresses = dressList;

            response = JsonConvert.SerializeObject(combo);
            Console.WriteLine(response);
            return response;
        }

        private static string addDresses(int users_id, int size, string title, string condition)
        {

            string query = "insert into dress(users_id, size, title, condition) values(" + users_id + "," + size + ",'" + title + "','" + condition + "')";

            int noOfRowInserted = db.Database.ExecuteSqlCommand(query);

            return noOfRowInserted + "";
        }


        /* private static string addDresses(int users_id, int size, string dress_desc, string photo_byte)
         {
             byte[] pByte = Encoding.ASCII.GetBytes(photo_byte);

             string query = "insert into dress(users_id, size, title, condition) values(" + users_id + "," + size + ",'" + dress_desc + "')";

             int noOfRowInserted = db.Database.ExecuteSqlCommand(query);

             int length = photo_byte.Length;

             System.IO.File.WriteAllText(@"D:\Projects\Dress_Exchange\pics\text.txt", photo_byte);

             return noOfRowInserted + "";
         }*/

        private static string likeAction(int user, int liked)
        {
            string response = "No match";
            bool match = false;
            var exists = (from like in db.likes
                          where like.users_id == liked
                          select like.liked_user_id).ToList();

            foreach (var click in exists)
            {
                if (user == click)
                {
                    match = true;
                    response = "There is a match!";
                }
            }

            var duplicate = (from like in db.likes
                             where like.users_id == user
                                && like.liked_user_id == liked
                             select like.users_id).ToList();

            if (!match && (duplicate.Count == 0))
            {
                string query = "insert into likes(users_id, liked_user_id) values(" + user + "," + liked + ")";
                int noOfRowInserted = db.Database.ExecuteSqlCommand(query);
            }

            return response;
        }

        private static string liked(int user)
        {
            string response = string.Empty;

            var result = (from like in db.likes
                          where like.liked_user_id == user
                          select like.users_id).ToList();

            foreach (var like in result)
            {
                response += like;
                response += ";";
            }
            string test = "method";
            return response;
        }


    }


}