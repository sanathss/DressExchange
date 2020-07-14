using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace DressClient
{
    class Program
    {
        private static Socket _clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        private static Boolean active = false;
        private static int user = 0;
        private static int dress = 0;

        static void Main(string[] args)
        {
            Console.Title = "Client";
            LoopConnect();

            Console.ReadLine();
        }

        private static void SendLoop()
        {
            while (true)
            {
                Thread.Sleep(5000);
                if (active)
                {
                    byte[] buffer = Encoding.ASCII.GetBytes("location;123456;-36.868392,174.777788");//-36.866628,174.776934");
                    _clientSocket.Send(buffer);

                    byte[] receivedBuf = new byte[1024];
                    int rec = _clientSocket.Receive(receivedBuf);
                    byte[] data = new byte[rec];
                    Array.Copy(receivedBuf, data, rec);
                    Console.WriteLine("Received: " + Encoding.ASCII.GetString(data));
                }
            }
        }

        private static void LoopConnect()
        {
            int attempts = 0;
            IPAddress serverIP = IPAddress.Parse("127.0.0.1");
            IPEndPoint remote = new IPEndPoint(serverIP, 3000);
            while (!_clientSocket.Connected)
            {
                try
                {
                    attempts++;
                    _clientSocket.Connect(remote);
                }
                catch (SocketException)
                {
                    Console.Clear();
                    Console.WriteLine("Attempts: " + attempts.ToString());
                }
            }
            Console.Clear();
            userInput();
            SendLoop();
        }

        public static async Task userInput()
        {
            await Task.Run(() =>
            {
                Console.WriteLine("Listening...");
                String value = Console.ReadLine();

                if (value.Equals("ping"))
                {
                    Console.WriteLine("pinging....");

                    //Dummy Values
                    byte[] buffer = Encoding.ASCII.GetBytes("ping");
                    _clientSocket.Send(buffer);

                    byte[] receivedBuf = new byte[1024];
                    int rec = _clientSocket.Receive(receivedBuf);
                    byte[] data = new byte[rec];
                    Array.Copy(receivedBuf, data, rec);
                    Console.WriteLine("Received: " + Encoding.ASCII.GetString(data));

                }
                else
                {
                    Console.WriteLine("Input does not exist.");
                }
            });
            userInput();

        }

    }
}
