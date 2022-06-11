module.exports = (io) => {

    const namespace = io.of('/orders/delivery');
    namespace.on('connection', function(socket) {

        console.log('USUARIO SE CONECTO A SOCKET IO');

        socket.on('position', function(data){
            console.log('SE EMITIO', JSON.parse(data));

            const d = JSON.parse(data); // Debe enviarla el cliente (ID, LAT, LNG) double
            namespace.emit(`position/${d.id_order}`, {id_order:d.id_order, lat:d.lat, lng:d.lng}); //Emite a kotlin
        })

        socket.on('disconnect', function(data) {
            console.log('USUARIO SE DESCONECTO DE SOCKET IO');
        })

    })
}