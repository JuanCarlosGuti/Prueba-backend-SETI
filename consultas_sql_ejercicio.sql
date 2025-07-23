-- =====================================================
-- CONSULTAS SQL - EJERCICIO BTG
-- Base de datos: BTG
-- =====================================================

-- CONSULTA PRINCIPAL SOLICITADA:
-- "Obtener los nombres de los clientes que tienen inscrito algún producto 
-- disponible solo en las sucursales que visitan"

SELECT DISTINCT c.nombre, c.apellidos
FROM Cliente c
WHERE EXISTS (
    -- Buscar productos inscritos del cliente
    SELECT 1 
    FROM Inscripcion i 
    WHERE i.idCliente = c.id
    AND NOT EXISTS (
        -- Verificar que NO existe disponibilidad en sucursales que NO visita
        SELECT 1 
        FROM Disponibilidad d 
        WHERE d.idProducto = i.idProducto
        AND d.idSucursal NOT IN (
            -- Sucursales que el cliente visita
            SELECT v.idSucursal 
            FROM Visitan v 
            WHERE v.idCliente = c.id
        )
    )
);

-- =====================================================
-- CONSULTAS ADICIONALES DE PRÁCTICA
-- =====================================================

-- 1. Listar todos los clientes con sus ciudades
SELECT id, nombre, apellidos, ciudad 
FROM Cliente;

-- 2. Mostrar todos los productos disponibles
SELECT id, nombre, tipoProducto 
FROM Producto;

-- 3. Listar todas las sucursales
SELECT id, nombre, ciudad 
FROM Sucursal;

-- 4. Clientes que han visitado al menos una sucursal
SELECT DISTINCT c.nombre, c.apellidos
FROM Cliente c
INNER JOIN Visitan v ON c.id = v.idCliente;

-- 5. Productos en los que está inscrito cada cliente
SELECT c.nombre, c.apellidos, p.nombre as producto
FROM Cliente c
INNER JOIN Inscripcion i ON c.id = i.idCliente
INNER JOIN Producto p ON i.idProducto = p.id;

-- 6. Sucursales donde está disponible cada producto
SELECT p.nombre as producto, s.nombre as sucursal, s.ciudad
FROM Producto p
INNER JOIN Disponibilidad d ON p.id = d.idProducto
INNER JOIN Sucursal s ON d.idSucursal = s.id;

-- 7. Clientes que han visitado sucursales en su misma ciudad
SELECT c.nombre, c.apellidos, c.ciudad, s.nombre as sucursal
FROM Cliente c
INNER JOIN Visitan v ON c.id = v.idCliente
INNER JOIN Sucursal s ON v.idSucursal = s.id
WHERE c.ciudad = s.ciudad;

-- 8. Productos disponibles en sucursales que visita un cliente específico
SELECT p.nombre as producto, s.nombre as sucursal
FROM Producto p
INNER JOIN Disponibilidad d ON p.id = d.idProducto
INNER JOIN Sucursal s ON d.idSucursal = s.id
WHERE s.id IN (
    SELECT v.idSucursal 
    FROM Visitan v 
    WHERE v.idCliente = 1  -- Cambiar por el ID del cliente deseado
);

-- 9. Clientes que no han visitado ninguna sucursal
SELECT c.nombre, c.apellidos
FROM Cliente c
WHERE NOT EXISTS (
    SELECT 1 
    FROM Visitan v 
    WHERE v.idCliente = c.id
);

-- 10. Productos que no están disponibles en ninguna sucursal
SELECT p.nombre, p.tipoProducto
FROM Producto p
WHERE NOT EXISTS (
    SELECT 1 
    FROM Disponibilidad d 
    WHERE d.idProducto = p.id
);

-- 11. Sucursales que no tienen ningún producto disponible
SELECT s.nombre, s.ciudad
FROM Sucursal s
WHERE NOT EXISTS (
    SELECT 1 
    FROM Disponibilidad d 
    WHERE d.idSucursal = s.id
);

-- 12. Clientes que están inscritos en productos no disponibles en ninguna sucursal
SELECT c.nombre, c.apellidos, p.nombre as producto
FROM Cliente c
INNER JOIN Inscripcion i ON c.id = i.idCliente
INNER JOIN Producto p ON i.idProducto = p.id
WHERE NOT EXISTS (
    SELECT 1 
    FROM Disponibilidad d 
    WHERE d.idProducto = p.id
);

-- 13. Contar cuántos productos tiene inscrito cada cliente
SELECT c.nombre, c.apellidos, COUNT(i.idProducto) as total_productos
FROM Cliente c
LEFT JOIN Inscripcion i ON c.id = i.idCliente
GROUP BY c.id, c.nombre, c.apellidos;

-- 14. Contar cuántas sucursales visita cada cliente
SELECT c.nombre, c.apellidos, COUNT(DISTINCT v.idSucursal) as total_sucursales
FROM Cliente c
LEFT JOIN Visitan v ON c.id = v.idCliente
GROUP BY c.id, c.nombre, c.apellidos;

-- 15. Productos más populares (más inscripciones)
SELECT p.nombre, p.tipoProducto, COUNT(i.idCliente) as total_inscripciones
FROM Producto p
LEFT JOIN Inscripcion i ON p.id = i.idProducto
GROUP BY p.id, p.nombre, p.tipoProducto
ORDER BY total_inscripciones DESC;

-- =====================================================
-- CONSULTAS COMPLEJAS ADICIONALES
-- =====================================================

-- 16. Clientes que han visitado TODAS las sucursales
SELECT c.nombre, c.apellidos
FROM Cliente c
WHERE NOT EXISTS (
    SELECT s.id 
    FROM Sucursal s
    WHERE NOT EXISTS (
        SELECT 1 
        FROM Visitan v 
        WHERE v.idCliente = c.id AND v.idSucursal = s.id
    )
);

-- 17. Productos disponibles en TODAS las sucursales
SELECT p.nombre, p.tipoProducto
FROM Producto p
WHERE NOT EXISTS (
    SELECT s.id 
    FROM Sucursal s
    WHERE NOT EXISTS (
        SELECT 1 
        FROM Disponibilidad d 
        WHERE d.idProducto = p.id AND d.idSucursal = s.id
    )
);

-- 18. Clientes que están inscritos en TODOS los productos
SELECT c.nombre, c.apellidos
FROM Cliente c
WHERE NOT EXISTS (
    SELECT p.id 
    FROM Producto p
    WHERE NOT EXISTS (
        SELECT 1 
        FROM Inscripcion i 
        WHERE i.idCliente = c.id AND i.idProducto = p.id
    )
);

-- 19. Sucursales que tienen TODOS los productos disponibles
SELECT s.nombre, s.ciudad
FROM Sucursal s
WHERE NOT EXISTS (
    SELECT p.id 
    FROM Producto p
    WHERE NOT EXISTS (
        SELECT 1 
        FROM Disponibilidad d 
        WHERE d.idSucursal = s.id AND d.idProducto = p.id
    )
);

-- 20. Clientes que solo visitan sucursales en su ciudad
SELECT c.nombre, c.apellidos, c.ciudad
FROM Cliente c
WHERE NOT EXISTS (
    SELECT 1 
    FROM Visitan v
    INNER JOIN Sucursal s ON v.idSucursal = s.id
    WHERE v.idCliente = c.id AND s.ciudad != c.ciudad
);

-- =====================================================
-- CONSULTAS CON FECHAS (si se agregan)
-- =====================================================

-- 21. Clientes que han visitado sucursales en los últimos 30 días
-- (Asumiendo que fechaVisita es de tipo DATE)
SELECT DISTINCT c.nombre, c.apellidos
FROM Cliente c
INNER JOIN Visitan v ON c.id = v.idCliente
WHERE v.fechaVisita >= CURRENT_DATE - INTERVAL 30 DAY;

-- 22. Sucursales más visitadas en el último mes
SELECT s.nombre, s.ciudad, COUNT(v.idCliente) as total_visitas
FROM Sucursal s
INNER JOIN Visitan v ON s.id = v.idSucursal
WHERE v.fechaVisita >= CURRENT_DATE - INTERVAL 30 DAY
GROUP BY s.id, s.nombre, s.ciudad
ORDER BY total_visitas DESC;

-- =====================================================
-- NOTAS IMPORTANTES:
-- =====================================================
-- 1. Las consultas asumen que las tablas existen con la estructura mostrada
-- 2. Los nombres de columnas y tablas deben coincidir exactamente
-- 3. La consulta principal es la más compleja y requiere subconsultas anidadas
-- 4. Algunas consultas pueden requerir ajustes según el motor de base de datos
-- 5. Para MySQL, usar CURRENT_DATE - INTERVAL 30 DAY
-- 6. Para PostgreSQL, usar CURRENT_DATE - INTERVAL '30 days'
-- 7. Para SQL Server, usar DATEADD(day, -30, GETDATE()) 