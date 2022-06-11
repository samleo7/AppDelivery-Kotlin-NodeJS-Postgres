//Haciendo las conexiones al servidor ----> Es decir las direccines y URL
const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app);
const logger = require('morgan');
const cors = require('cors');
const passport = require('passport');
const multer = require('multer');
const serviceAccount = require('./serviceAccountKey.json');
const admin = require('firebase-admin');
const io = require('socket.io')(server);
const ordersDeliverySocket = require('./sockets/orders_delivery_socket');

//Inicializando el proyecto de Node JS para que se configure  con  FireBase
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

//Almacenar una imagen temporalmente hasta que se suba a firebase
const upload = multer({
    storage: multer.memoryStorage()
});

/*
* ESTABLECIENDO RUTAS
*/
const users = require('./routes/usersRoutes');
const categories = require('./routes/categoriesRoutes');
const products = require('./routes/productsRoutes');
const address = require('./routes/addressRoutes');
const orders = require('./routes/ordersRoutes');


const port = process.env.PORT || 3000;

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({
    extended: true
}));

app.use(cors());
app.use(passport.initialize());
app.use(passport.session());

require('./config/passport')(passport);

app.disable('x-powered-by');

app.set('port', port);

/*
* LLAMANDO A LAS RUTAS
*/
users(app, upload);
categories(app,upload);
products(app,upload)
address(app);
orders(app);

/*
* LLAMAR A LOS SOCKETS
*/
ordersDeliverySocket(io);


/*server.listen(3000, '192.168.0.18' || 'localhost', function() {
    console.log('Aplicacion de NodeJS ' + port + ' Iniciada...')
}); */

server.listen(port, function() {
    console.log('Listening on port ' + port)
});

//Rutas
app.get('/', (req, res) => {
    res.send('Ruta raiz del backend');
});

app.get('/test', (req, res) => {
    res.send('Este es la ruta TEST');
});


//ERROR HANDLER
app.use((err, req, res, next) => {
    console.log(err)
    res.status(err.status || 500).send(err.stack);
});

module.exports = {
    app: app,
    server: server
}

// 200 -> Es una respuesta exitosa
// 400 -> Significa que la url no exite
// 500 -> Error interno del servidor
