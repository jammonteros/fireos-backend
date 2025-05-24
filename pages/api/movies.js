export default function handler(req, res) {
  // Configurar CORS
  res.setHeader('Access-Control-Allow-Credentials', true);
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET,OPTIONS,PATCH,DELETE,POST,PUT');
  res.setHeader(
    'Access-Control-Allow-Headers',
    'X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version'
  );

  // Manejar preflight request
  if (req.method === 'OPTIONS') {
    res.status(200).end();
    return;
  }

  const movies = [
    {
      id: "1",
      title: "No Te Muevas",
      description: "Una película de suspenso que te mantendrá al borde del asiento.",
      imageUrl: "/images/muevas.jpg",
      category: "Suspenso",
      rating: 4.5,
      year: 2024
    },
    {
      id: "2",
      title: "Código T",
      description: "Una historia de acción y misterio que te sorprenderá.",
      imageUrl: "/images/codigo.jpg",
      category: "Acción",
      rating: 4.8,
      year: 2024
    },
    {
      id: "3",
      title: "Linda",
      description: "Una comedia romántica que te hará reír y llorar.",
      imageUrl: "/images/linda.jpg",
      category: "Comedia",
      rating: 4.2,
      year: 2024
    }
  ];

  res.status(200).json(movies);
} 