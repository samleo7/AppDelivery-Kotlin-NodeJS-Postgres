const User = require('../models/user');
const Rol = require('../models/rol');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const keys = require('../config/keys');
const storage = require('../utils/cloud_storage');


module.exports = {

    async getAll(req, res, next){
        try{
            const data = await User.getAll();
            console.log(`Usuarios: ${data}`);
            return res.status(201).json(data);
        }
        catch (error){
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Error al obtener los usuarios'
            });

        }
    },

    async findDeliveryMen(req, res, next){
        try{
            const data = await User.findDeliveryMen();
            return res.status(201).json(data);
        }
        catch (error){
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Error al obtener los usuarios'
            });

        }
    },

    async register(req, res, next){
        try{
            const user = req.body;
            const data = await User.create(user);

            //Registrando el ROL del usuario por defecto como CLIENTE
            await Rol.create(data.id, 1);
            const token = jwt.sign({ id: data.id, email: user.email }, keys.secretOrKey, {
                // expiresIn
            })

            const myData = {
                id: data.id,
                name: user.name,
                lastname:user.lastname,
                email: user.email,
                phone: user.phone,
                image: user.image,
                session_token: `JWT ${token}`
            };


            return res.status(201).json({
                success: true,
                message: 'El registro se realizo correctamente',
                data: myData
                
            });
        }
        catch(error){
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Hubo un error con el registro del usuario',
                error:error
            });
        }
    },

    async login(req, res, next) {

        try{

            const email = req.body.email;
            const password = req.body.password;

            const myUser = await User.findByEmail(email);

            if(!myUser) {
                return res.status(401).json({
                    success:false,
                    message:'El email no fue encontrado'
                })
            }

            const isPasswordValid = await bcrypt.compare(password, myUser.password);

            if(isPasswordValid){
                const token = jwt.sign({ id: myUser.id,email: myUser.email }, keys.secretOrKey, {
                    // expiresIn
                })

                const data = {
                    id: myUser.id,
                    name: myUser.name,
                    lastname:myUser.lastname,
                    email: myUser.email,
                    phone: myUser.phone,
                    image: myUser.image,
                    session_token: `JWT ${token}`,
                    //Se agrega un nuevo atributo por la relacion entre rol y tipo rol
                    roles: myUser.roles
                };

                //Guardando el token
                await User.updateSessionToken(myUser.id, `JWT ${token}`);

            //    console.log('USUARIO ENVIADO ${data}');

                return res.status(201).json({
                    success:true,
                    message: 'El usuario ha sido autenticado',
                    data:data
                });
            }

            else{
                //UNAUTHORIZED
                return res.status(401).json({
                    success: false,
                    message: 'La contraseña es incorrecta',
                });
            }


        }
        catch(error){
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Hubo un error con el login del usuario',
                error:error
            });
        }
    },

    async update(req, res, next) {

        try{

            console.log('Usuario', req.body.user);

            const user = JSON.parse(req.body.user); // Cliente debe enviarnos un objeto user
            console.log('Usuario Parseando', user);

            const files = req.files;

            if(files.length > 0){ // Cliente nos envia un archivo

                const pathImage = `image_${Date.now()}`; // Nombre del archivo
                const url = await storage(files[0], pathImage);

                if(url != undefined && url != null){
                    user.image = url;
                }

            }

            await User.update(user); // Guardando la URL en la base de datos

            return res.status(201).json({
                success: true,
                message: 'Los datos del usuario se han actualizado correctamente',
                data:user
            });

        }
        catch(error){
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Hubo un error al actulizar los datos del usuario',
                error:error
            });
        }
        
    },

    async updateWithoutImage(req, res, next) {

        try{

            console.log('Usuario', req.body);

            const user = req.body; // Cliente debe enviarnos un objeto user
            console.log('Usuario Parseando', user);

            await User.update(user); // Guardando la URL en la base de datos

            return res.status(201).json({
                success: true,
                message: 'Los datos del usuario se han actualizado correctamente',
                data:user
            });

        }
        catch(error){
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Hubo un error al actulizar los datos del usuario',
                error:error
            });
        }
        
    },

    async updateNotificationToken(req, res, next) {

        try{

            const user = req.body; //CLIENTE

            await User.updateNotificationToken(user.id, user.notification_token)

            return res.status(201).json({
                success: true,
                message: 'El token de notificaciones se ha almacenado correctamente',
            });

        }
        catch(error){
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'Hubo un error al actulizar el token de notificaciones',
                error:error
            });
        }
        
    }
};