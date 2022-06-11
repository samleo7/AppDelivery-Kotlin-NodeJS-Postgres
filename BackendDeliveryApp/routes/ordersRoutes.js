const OrdersController = require('../controllers/ordersController');
const passport = require('passport')

module.exports = (app) => {

    //TRAER DATOS
    app.get('/api/orders/findByStatus/:status', passport.authenticate('jwt', {session: false}), OrdersController.findByStatus);
    app.get('/api/orders/findByClientAndStatus/:id_client/:status', passport.authenticate('jwt', {session: false}), OrdersController.findByClienAndtStatus);
    app.get('/api/orders/findByDeliveryAndStatus/:id_delivery/:status', passport.authenticate('jwt', {session: false}), OrdersController.findByDeliveryAndtStatus);

    //GUARDAR O CREAR DATOS
    app.post('/api/orders/create', passport.authenticate('jwt', {session: false}), OrdersController.create);

    //ACTUALIZAR
    app.put('/api/orders/updateToDispatched', passport.authenticate('jwt', {session: false}), OrdersController.updateToDispatched);
    app.put('/api/orders/updateToOnTheWay', passport.authenticate('jwt', {session: false}), OrdersController.updateToOnTheWay);
    app.put('/api/orders/updateToDelivered', passport.authenticate('jwt', {session: false}), OrdersController.updateToDelivered);
    app.put('/api/orders/updateLatLng', passport.authenticate('jwt', {session: false}), OrdersController.updateLatLng);



  
    //ACTUALIZAR DATOS
    // 401 UNAUTHORIZED
}