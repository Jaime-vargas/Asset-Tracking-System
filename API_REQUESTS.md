# API Requests

### Clients

  ```
    REQUEST
  {
  "name": "Empresa Ejemplo"
  }
  
  RESPONSE 
  {
  "id": 1,
  "name": "Empresa Ejemplo",
  "sucursalList": [
    "Sucursal Centro",
    "Sucursal Norte"
    ]
  }
  ```

- *GET* **/api/v1/clients**
  - Description: Retrieve a list of all clients.
  - Response: JSON array of client objects.
---
- *POST* **/api/v1/clients/**
  - Description: Create a new client.
  - Response: JSON object of the created client.
---
- *PUT* **/api/v1/clients/{client_id}**
  - Description: Update an existing client.
  - Parameters:
    - client_id (path): The ID of the client to update.
  - Response: JSON object of the updated client.

### Sucursal


Recursos
clients
sucursales
cameras
hardware
reports
comments
photos
users
Cada uno con:
GET list
GET by id
POST
PUT/PATCH
DELETE
Errores

ApiException base

ErrorResponse estándar
¿Existe? → 404

¿Duplicado? → 409

¿Formato malo? → 400

¿Estado no válido? → 409

¿No autenticado? → 401

¿Sin permiso? → 403