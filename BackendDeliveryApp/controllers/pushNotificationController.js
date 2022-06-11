const https = require('https');
const admin = require('firebase-admin');
const serviceAccount = require('../serviceAccountKey.json');

/*admin.initializeApp({
    credential:admin.credential.cert(serviceAccount)
})*/

module.exports = {

    sendNotification(token, data){

        const notification = JSON.stringify({
            'to': token,
            "data": {
                'title': data.title,
                'body': data.body,
                'id_notification': data.id_notification,
            },
            'priority':'high',
            'ttl': '4500s'
        });

        const options = {
            hostname: 'from.googleapis.com',
            path:'/fcm/send',
            method:'POST',
            port:443,
            headers:{
                'Content-Type':'application/json',
                'Authorization':'key=AAAAl-K59ek:APA91bFLKDAYmBv9b1hn_zwkGKCY6YR9G3Lx9IApR8NDwwoCH6F1nMMKAV1bepxha7TwQqAuQ3i_UBHm7qmuxhW4__7PBmUo8O7PF6It36F_Eih1gO-f62Kb7ZTgO_zRQbFGU1rcmFM0'
            }
        }

        const req = https.request(options, (res) =>{
            console.log('Status code Notification', res.statusCode);
            res.on('data', (d) => {
                process.stdout.write(d)
            })
        })

        req.on('error', (error) =>{
            console.error(error)
        })
        req.write(notification);
        req.end();

    },

    sendNotificationToMultipleDevices(tokens, data){

        const notification = JSON.stringify({
            'registration_ids': tokens,
            "data": {
                'title': data.title,
                'body': data.body,
                'id_notification': data.id_notification,
            },
            'priority':'high',
            'ttl': '4500s'
        });

        const options = {
            hostname: 'from.googleapis.com',
            path:'/fcm/send',
            method:'POST',
            port:443,
            headers:{
                'Content-Type':'application/json',
                'Authorization':'key=AAAAl-K59ek:APA91bFLKDAYmBv9b1hn_zwkGKCY6YR9G3Lx9IApR8NDwwoCH6F1nMMKAV1bepxha7TwQqAuQ3i_UBHm7qmuxhW4__7PBmUo8O7PF6It36F_Eih1gO-f62Kb7ZTgO_zRQbFGU1rcmFM0'
            }
        }

        const req = https.request(options, (res) =>{
            console.log('Status code Notification', res.statusCode);
            res.on('data', (d) => {
                process.stdout.write(d)
            })
        })

        req.on('error', (error) =>{
            console.error(error)
        })
        req.write(notification);
        req.end();

    }


}