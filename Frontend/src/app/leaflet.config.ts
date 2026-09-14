import * as L from 'leaflet';

// Configuración de íconos para Leaflet
const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';

export const defaultIcon = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41],
});

// Aplicar el ícono por defecto a todos los marcadores
if (L && L.Marker && L.Marker.prototype) {
  (L.Marker.prototype as any).options = {
    ...(L.Marker.prototype as any).options,
    icon: defaultIcon,
  };
}
