// Conexion a la BD   ---> Se tuvo que descargar paquetes(bluebird, pg-promise ) desde consola
const promise = require('bluebird');
const options = {
    promiseLib:promise,
    query:(e) => {}
}

const pgp = require('pg-promise')(options);
const types = pgp.pg.types;
types.setTypeParser(1114, function(stringValue){
    return stringValue;
});

/*const databaseConfig = {
    'host': '127.0.0.1',
    'port': 5432,
    'database': 'delivery_db',
    'user': 'postgres',
    'password': '123456'
}; */

const databaseConfig = {
    'host': 'ec2-18-215-96-22.compute-1.amazonaws.com',
    'port': 5432,
    'database': 'deubn4ru7t1os3',
    'user': 'oanogektojkmin',
    'password': '9223cba137ae32e3663d6aa8eed00fd78a3f6b418062fcf08e2b0772a482b398',
    'ssl': {rejectUnauthorized: false}
};


const db = pgp(databaseConfig);

module.exports = db