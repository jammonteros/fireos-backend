export default function handler(req, res) {
  const movies = [
    {
      id: "1",
      title: "No Te Muevas",
      description: "Una película de suspenso que te mantendrá al borde del asiento.",
      imageUrl: "https://tu-url-de-vercel.com/images/NoTeMuevas.jpg",
      category: "Suspenso",
      rating: 4.5,
      year: 2024
    },
    {
      id: "2",
      title: "Código T",
      description: "Una historia de acción y misterio que te sorprenderá.",
      imageUrl: "https://tu-url-de-vercel.com/images/Codigo-T-rojo.jpg",
      category: "Acción",
      rating: 4.8,
      year: 2024
    },
    {
      id: "3",
      title: "Linda",
      description: "Una comedia romántica que te hará reír y llorar.",
      imageUrl: "https://tu-url-de-vercel.com/images/linda-334322519-large.jpg",
      category: "Comedia",
      rating: 4.2,
      year: 2024
    }
  ];

  res.status(200).json(movies);
} 