DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE roles(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL UNIQUE,
	image VARCHAR(255) NULL,
	route VARCHAR(255) NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL
);


DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users(
	id BIGSERIAL PRIMARY KEY,
	email VARCHAR(255) NOT NULL UNIQUE,
	name VARCHAR(255) NOT NULL,
	lastname VARCHAR(255) NOT NULL,
	phone VARCHAR(80) NOT NULL UNIQUE,
	image VARCHAR(255) NULL,
	password VARCHAR(255) NOT NULL,
	is_available BOOLEAN NULL,
	session_token VARCHAR(255) NULL,
	notification_token VARCHAR(255) NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL
);

DROP TABLE IF EXISTS user_has_roles CASCADE;
CREATE TABLE user_has_roles(
	id_user BIGSERIAL NOT NULL,
	id_rol BIGSERIAL NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_user) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_rol) REFERENCES roles(id) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(id_user, id_rol)
);


INSERT INTO roles(
	name,
	route,
	image,
	created_at,
	updated_at
)
VALUES(
	'CLIENTE',
	'client/home',
	'https://findicons.com/files/icons/573/must_have/256/user.png',
	'2022-02-19',
	'2022-02-19'
);

INSERT INTO roles(
	name,
	route,
	image,
	created_at,
	updated_at
)
VALUES(
	'RESTAURANTE',
	'restaurant/home',
	'https://cdn.iconscout.com/icon/free/png-256/restaurant-1495593-1267764.png',
	'2022-02-19',
	'2022-02-19'
);

INSERT INTO roles(
	name,
	route,
	image,
	created_at,
	updated_at
)
VALUES(
	'REPARTIDOR',
	'delivery/home',
	'https://www.dondeluchito.cl/wp-content/uploads/2018/10/moto-deli.png',
	'2022-02-19',
	'2022-02-19'
);


SELECT 
        U.id, 
        U.email, 
        U.name, 
        U.lastname, 
        U.image, 
        U.phone, 
        U.password, 
        U.session_token,
		json_agg(
			json_build_object(
				'id', R.id,
				'name', R.name,
				'image', R.image,
				'route', R.route
			) 
		) AS roles
    FROM 
        users AS U
	INNER JOIN user_has_roles AS UHR
	ON UHR.id_user = U.id
	
	INNER JOIN roles AS R
	ON R.id = UHR.id_rol
	
    WHERE 
        U.email = 'camila@gmail.com'
		
	GROUP BY
		U.id


DROP TABLE IF EXISTS categories CASCADE;

CREATE TABLE categories(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL UNIQUE,
	image VARCHAR (255) NOT NULL, 
	created_at TIMESTAMP(0) NOT NULL,	
	updated_at TIMESTAMP(0) NOT NULL

);

DROP TABLE IF EXISTS products CASCADE;
CREATE TABLE products(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(180) NOT NULL UNIQUE,
	description VARCHAR(255) NOT NULL,
	price DECIMAL DEFAULT 0,
	image1 VARCHAR(255) NULL,
	image2 VARCHAR(255) NULL, 
	image3 VARCHAR(255) NULL,  
	id_category BIGINT NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,	
	updated_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_category) REFERENCES categories(id) ON UPDATE CASCADE ON DELETE CASCADE

);

SELECT
	P.id,
	P.name,
	P.description,
	P.price,
	P.image1,
	P.image2,
	P.image3,
	P.id_category
FROM
	products AS P
INNER JOIN
	categories AS C
ON
	P.id_category = C.id
WHERE
	C.id = 1


DROP TABLE IF EXISTS address CASCADE;
CREATE TABLE address(
	id BIGSERIAL PRIMARY KEY,
	id_user BIGINT NOT NULL,
	address VARCHAR(255) NOT NULL,
	neighborhood VARCHAR(255) NOT NULL,
	lat DECIMAL DEFAULT 0,
	lng DECIMAL DEFAULT 0,
	created_at TIMESTAMP(0) NOT NULL,	
	updated_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_user) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE

);	

SELECT
	id,
	id_user,
	address,
	neighborhood,
	lat,
	lng
FROM
	address
WHERE
	id_user = 1


DROP TABLE IF EXISTS orders CASCADE;
CREATE TABLE orders(
	id BIGSERIAL PRIMARY KEY,
	id_client BIGINT NOT NULL,
	id_delivery BIGINT NULL,
	id_address BIGINT NOT NULL,
	lat DECIMAL DEFAULT 0,
	lng DECIMAL DEFAULT 0,
	status VARCHAR(90) NOT NULL,
	timestamp BIGINT NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,	
	updated_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_client) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_delivery) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_address) REFERENCES address(id) ON UPDATE CASCADE ON DELETE CASCADE
	
);

DROP TABLE IF EXISTS order_has_products CASCADE;
CREATE TABLE order_has_products(
	id_order BIGINT NOT NULL,
	id_product BIGINT NOT NULL,
	quantity BIGINT NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,	
	updated_at TIMESTAMP(0) NOT NULL,
	PRIMARY KEY(id_order, id_product),
	FOREIGN KEY(id_order) REFERENCES orders(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_product) REFERENCES products(id) ON UPDATE CASCADE ON DELETE CASCADE
);


SELECT
	O.id,
	O.id_client,
	O.id_address,
	O.id_delivery,
	O.status,
	O.timestamp,
	JSON_BUILD_OBJECT(
		'id', U.id,
		'name', U.name,
		'lastname', U.lastname,
		'image', U.image
	) AS client,
	JSON_BUILD_OBJECT(
		'id', A.id,
		'name', A.address,
		'lastname', A.neighborhood,
		'lat', A.lat,
		'lng', A.lng
	) AS address
	
FROM
	orders AS O
INNER JOIN
	users AS U
ON 
	O.id_client = U.id
INNER JOIN
	address AS A
ON
	A.id = O.id_address
WHERE
	status = 'PAGADO';