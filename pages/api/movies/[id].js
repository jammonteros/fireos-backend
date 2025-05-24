export default function handler(req, res) {
  const { id } = req.query;

  const movies = {
    "1": {
      id: "1",
      title: "No Te Muevas",
      description: "Una película de suspenso que te mantendrá al borde del asiento.",
      imageUrl: "https://tu-url-de-vercel.com/images/NoTeMuevas.jpg",
      category: "Suspenso",
      rating: 4.5,
      year: 2024,
      duration: "2h 15min",
      director: "Director Ejemplo",
      cast: ["Actor 1", "Actor 2", "Actor 3"],
      videoUrl: "https://tu-url-de-vercel.com/videos/NoTeMuevas.mp4"
    },
    "2": {
      id: "2",
      title: "Código T",
      description: "Una historia de acción y misterio que te sorprenderá.",
      imageUrl: "https://tu-url-de-vercel.com/images/Codigo-T-rojo.jpg",
      category: "Acción",
      rating: 4.8,
      year: 2024,
      duration: "1h 45min",
      director: "Director Ejemplo 2",
      cast: ["Actor 4", "Actor 5", "Actor 6"],
      videoUrl: "https://tu-url-de-vercel.com/videos/CodigoT.mp4"
    },
    "3": {
      id: "3",
      title: "Linda",
      description: "Una comedia romántica que te hará reír y llorar.",
      imageUrl: "https://tu-url-de-vercel.com/images/linda-334322519-large.jpg",
      category: "Comedia",
      rating: 4.2,
      year: 2024,
      duration: "1h 55min",
      director: "Director Ejemplo 3",
      cast: ["Actor 7", "Actor 8", "Actor 9"],
      videoUrl: "https://tu-url-de-vercel.com/videos/Linda.mp4"
    }
  };

  const movie = movies[id];

  if (movie) {
    res.status(200).json(movie);
  } else {
    res.status(404).json({ message: "Película no encontrada" });
  }
} 