-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 19-04-2021 a las 00:32:43
-- Versión del servidor: 10.4.17-MariaDB
-- Versión de PHP: 8.0.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `plataforma_archivos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `acceso_directo`
--

CREATE TABLE `acceso_directo` (
  `id` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_publicacion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `acceso_directo`
--

INSERT INTO `acceso_directo` (`id`, `id_usuario`, `id_publicacion`) VALUES
(4, 1, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `publicaciones`
--

CREATE TABLE `publicaciones` (
  `id` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha_alta` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_final` date DEFAULT NULL,
  `prioridad` varchar(30) NOT NULL,
  `carpeta` varchar(255) NOT NULL,
  `estado` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `publicaciones`
--

INSERT INTO `publicaciones` (`id`, `id_usuario`, `titulo`, `descripcion`, `fecha_alta`, `fecha_inicio`, `fecha_final`, `prioridad`, `carpeta`, `estado`) VALUES
(2, 1, 'Publicacion de prueba', 'Hola esta es una descripcion', '2021-04-18', '2021-04-18', '2021-04-30', 'normal', 'http://localhost:8080/appData/documentos/PUBLICACION DE PRUEBA', 'abierta'),
(3, 1, 'Otra publicacion de prueba', 'Hola como estan esta es una dsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.', '2021-04-18', '2021-04-18', '2021-04-21', 'normal', 'http://localhost:8080/appData/documentos/OTRA PUBLICACION DE PRUEBA', 'abierta');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `paterno` varchar(100) NOT NULL,
  `materno` varchar(100) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `clave` varchar(255) NOT NULL,
  `rol` varchar(50) NOT NULL,
  `img` varchar(255) DEFAULT NULL,
  `codigo_seguridad` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `paterno`, `materno`, `correo`, `clave`, `rol`, `img`, `codigo_seguridad`) VALUES
(1, 'FERNANDO', 'TINOCO', 'RAMIREZ', 'fernando.tr96@outlook.es', '$2a$10$pjIzVL6LG3W5ZsjyO1tly.F0R2wcLT2darhdfKV9xA6AJKIbw2zIW', 'admin', 'http://localhost:8080/appData/uploads/FERNANDO_TINOCO_RAMIREZ/53-532602_best-desktop-background-hd-anime-anime-wallpapers-hd.jpg', 'KxOtc');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `acceso_directo`
--
ALTER TABLE `acceso_directo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_acceso_directo_publicaciones` (`id_publicacion`);

--
-- Indices de la tabla `publicaciones`
--
ALTER TABLE `publicaciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_publicaciones_usuarios` (`id_usuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `acceso_directo`
--
ALTER TABLE `acceso_directo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `publicaciones`
--
ALTER TABLE `publicaciones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `acceso_directo`
--
ALTER TABLE `acceso_directo`
  ADD CONSTRAINT `fk_acceso_directo_publicaciones` FOREIGN KEY (`id_publicacion`) REFERENCES `publicaciones` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `publicaciones`
--
ALTER TABLE `publicaciones`
  ADD CONSTRAINT `fk_publicaciones_usuarios` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
