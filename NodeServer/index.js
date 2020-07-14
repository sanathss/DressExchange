//Express Variables
const express = require('express')
const app = express()
//Setup Body Parser
const bodyParser = require('body-parser')
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())
//SQL Server Variables
const sql = require('mssql')
var config = {
    user: 'serverapp',
    password: '12345',
    server: '127.0.0.1', 
    database: 'dress_exchange'
}

//Setup String Builder
const stringBuilder = require('string-builder')
//Base Queries
const baseUserSelect = 'SELECT * FROM users WHERE facebook_login = \'{0}\''
const baseUserInsert = 'INSERT INTO users(facebook_login, email, fullname, size, suburb_id) VALUES (\'{0}\',\'{1}\',\'{2}\',{3},{4})'
const baseAddInsert = 'INSERT INTO dress(users_id, size, title, condition) VALUES ({0},{1},\'{2}\',\'{3}\')'
const baseMineSelect = 'SELECT TOP 1 * FROM dress WHERE users_id = \'{0}\''
const baseLikeInsert = 'INSERT INTO likes(users_id, liked_user_id) '
const baseLikeSelect = 'SELECT {0}, {1} '
const baseLikeUnion = 'UNION ALL '
const baseRefreshSelect = 'SELECT * FROM dress d LEFT JOIN likes l ON d.users_id = l.liked_user_id'

//Start connection to MSSQL DB
sql.connect(config, (err) => {
    if (err) console.log(err);
})

//Restarter
console.log('======================================================================================================================================================')
console.log(Date())
console.log('======================================================================================================================================================')

app.post('/', (req, res) => {
    console.log(req.body)
	res.status(200).json({
		message: "JSON Data received successfully"
	});
});

app.post('/login', (req, res) => {
    //Login/Signup
    console.log('Login/Signup')
    const json = req.body
    //SELECT Builder
    const select = new sql.Request()
    let sbs = new stringBuilder()
    sbs.appendFormat(baseUserSelect, json.facebook_login)

    select.query(sbs.toString(), (err, recordset) => {            
        if (err) console.log(err)
        if(recordset.rowsAffected == 0)
        {
            //INSERT Builder
            const insert = new sql.Request();
            let sbi = new stringBuilder()
            sbi.appendFormat(baseUserInsert, json.facebook_login, json.email, json.fullname, json.size, json.suburb_id)

            insert.query(sbi.toString(), (err, recordset) => {
                if (err) console.log(err)
                if (recordset.rowsAffected > 0) console.log('New User ' + json.facebook_login + ' added.')
                //Response
                res.send('User has been signed up.')
            })
        }
        else
        {
            //Response
            res.send('Logged in successfully.')
        }
    })
})

app.post('/addDress', (req, res) => {
    //Add new dress
    console.log('Add Dress')
    const json = req.body
    //INSERT Builder
    const insert = new sql.Request()
    let sbi = new stringBuilder()
    sbi.appendFormat(baseAddInsert, json.users_id, json.size, json.title, json.condition)

    insert.query(sbi.toString(), (err, recordset) => {
        if (err) console.log(err)
        if(recordset.rowsAffected == 0) console.log('Dress has been added.')
        //Response
        res.send('Dress has been added.')
    })
})

app.post('/mine', (req, res) => {
    //Retrieve users own uploaded dress
    console.log('Users Dress')
    const json = req.body
    //SELECT Builder
    const select = new sql.Request()
    let sbs = new stringBuilder()
    sbs.appendFormat(baseMineSelect, json.users_id)

    select.query(sbs.toString(), (err, recordset) => {
        if (err) console.log(err)
        //Send RecordSet to client
        res.send(recordset.recordset)
    })
})

app.post('/liked', (req, res) => {
    //Uploading like list of user
    console.log('Liked list')
    const json = req.body
    var user = json.users_id
    var likes = json.likeList
    //INSERT Builder
    const insert = new sql.Request()
    let sbi = new stringBuilder()
    sbi.append(baseLikeInsert)
    for(var i=0;i<likes.length;i++)
    {
        sbi.appendFormat(baseLikeSelect, user, likes[i])
        sbi.append(baseLikeUnion)
    }
    sbi.appendFormat(baseLikeSelect, user, likes[likes.length-1])
    
    insert.query(sbi.toString(), (err, recordset) => {
        if (err) console.log(err)
        //Response
        res.send('Liked!')
    })
})

app.post('/refresh', (req, res) => {
    //Retrieving list of dresses
    console.log('Dress list')
    const json = req.body
    //SELECT Builder
    const select = new sql.Request()
    let sbs = new stringBuilder()
    sbs.appendFormat(baseRefreshSelect)

    select.query(sbs.toString(), (err, recordset) => {
        if (err) console.log(err)
        //Response
        console.log(recordset.recordset)
        res.send(recordset.recordset)
    })
})

app.get('/test', (req, res) => {
    res.send('This is a test function called malwatte')
})

app.listen(3000, () => {
    console.log('Server is now listening...')
})
   
