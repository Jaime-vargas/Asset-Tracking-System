# DATASET

### Clients

Api url: /api/v1/clients

# Request
```
{
  "name": "SKS Enterprise Technologies"
}
```
# Response
```
{
  "id": 1,
  "name": "SKS Enterprise Technologies",
  "sucursalDTOList": [
    {
      "id": 1,
      "name": "Sucursal Guadalajara"
    },
    {
      "id": 2,
      "name": "Sucursal CDMX"
    }
  ]
}
```

### Sucursal   

Api url: /api/v1/clients/{client_id}/sucursal

# Request
```
{
  "name": "Sucursal Guadalajara"
}
```
# Response
```
{
  "id": 1,
  "name": "Sucursal Guadalajara"
}
```

## Hardware
### Camera

Api url: /api/v1/sucursal/{sucursal_id}/camera

# Request
```
{
  "cameraId": "CAM-004",
  "name": "Cámara Principal Acceso 4",
  "serialNumber": "SN-AX93-9989",
  "model": "Hikvision DS-2CD2143G0-I",
  "location": "Entrada principal",
  "macAddress": "00:1a:2B:3C:3a:5C",
  "ipAddress": "192.168.1.43"
 }
```
# Response
```
{
    "id": 1,
    "cameraId": "CAM-010",
    "name": "Cámara Caseta 1",
    "serialNumber": "SN-AX93-9986",
    "model": "Hikvision DS-2CD2143G0-I",
    "location": "Entrada principal",
    "lastMaintenanceDate": "N/A",
    "sucursal": "Sucursal Guadalajara",
    "cliente": "SKS Enterprise",
    "macAddress": "00:1A:2B:3C:3D:5A",
    "ipAddress": "192.168.1.51",
    "reports": []
}
```
## Reports
### Create Report
Api url: /api/v1/camera/{camera_id}/reports
# Request
```
{
  "reportType": "Mantenimiento Preventivo",
  "description": "Limpieza de lente y verificación de conexiones.",
  "date": "2024-06-15"
}
```
# Response
```
{
  "id": 1,
  "reportType": "Mantenimiento Preventivo",
  "description": "Limpieza de lente y verificación de conexiones.",     
    "date": "2024-06-15",
    "camera": "Cámara Principal Acceso 4"
}
```