package com.fenparser;

public class FENParser {
    public static class FENExcepcion extends Exception {
        public FENExcepcion(String mensaje) { super(mensaje); }
    }

    public static Tablero parse(String fen) throws FENExcepcion {

        if (fen == null) throw new FENExcepcion("La cadena FEN es nula.");
        fen = fen.trim();
        if (fen.isEmpty()) throw new FENExcepcion("La cadena FEN está vacía.");

        // Divide la cadena FEN en 6 partes principales
        String[] partes = fen.split("\\s+");
        if (partes.length != 6)
            throw new FENExcepcion("La cadena FEN debe tener 6 campos separados por espacios. Se encontraron: " + partes.length);

        String colocacion = partes[0];
        String mover_lado = partes[1];
        String enroque = partes[2];
        String captura = partes[3];
        String medio_movimiento = partes[4];
        String jugada_completa = partes[5];

        // Validar y procesar la colocación de las piezas (8 filas)
        String[] filas_h = colocacion.split("/");
        if (filas_h.length != 8)
            throw new FENExcepcion("La colocación de piezas debe tener 8 filas separadas por '/'. Se encontró: " + filas_h.length);

        char[][] tablero = new char[8][8];

        // Recorrer cada fila y validar sus caracteres
        for (int r = 0; r < 8; r++) {
            String fila_h = filas_h[r];
            int fila = 0;

            for (int i = 0; i < fila_h.length(); i++) {
                char c = fila_h.charAt(i);

                if (c >= '1' && c <= '8') {
                    int empty = c - '0';
                    if (fila + empty > 8) throw new FENExcepcion("Fila " + (8 - r) + " tiene demasiadas casillas.");
                    for (int k = 0; k < empty; k++) tablero[r][fila++] = '.';
                } else if (pieza_valida(c)) {
                    if (fila >= 8) throw new FENExcepcion("Fila " + (8 - r) + " demasiado larga.");
                    tablero[r][fila++] = c;
                } else {
                    throw new FENExcepcion("Caracter invalido '" + c + "' en fila " + (8 - r));
                }
            }
            if (fila != 8)
                throw new FENExcepcion("Fila " + (8 - r) + " no tiene exactamente 8 casillas");
        }

        // Valida el resto de campos (lado, enroque, captura al paso, contadores)

        if (!mover_lado.equals("w") && !mover_lado.equals("b"))
            throw new FENExcepcion("El lado que debe mover debe ser 'w' o 'b'. Se encontró:'" + mover_lado + "'.");

        if (!enroque.equals("-")) {
            for (char c : enroque.toCharArray())
                if ("KQkq".indexOf(c) == -1)
                    throw new FENExcepcion("Carácter de enroque inválido: '" + c + "'.");
        }

        if (!captura.equals("-")) {
            if (captura.length() != 2) throw new FENExcepcion("Casilla de captura al paso inválida.");
            char fila = captura.charAt(0);
            char fila_h = captura.charAt(1);
            if ("abcdefgh".indexOf(fila) == -1 || (fila_h != '3' && fila_h != '6'))
                throw new FENExcepcion("Casilla de captura al paso inválida. '" + captura + "'.");
        }

        // Contadores
        if (!no_negativo(medio_movimiento))
            throw new FENExcepcion("El contador de medio movimiento debe ser no negativo.");

        if (!es_positivo(jugada_completa))
            throw new FENExcepcion("El contador de jugadas completas debe ser positivo.");

        // Crea y llena el objeto Tablero con los datos validados
        Tablero b = new Tablero();
        b.setMover_lado(mover_lado.charAt(0));
        b.setEnroque(enroque);
        b.setCaptura(captura);
        b.setMedio_movimiento(Integer.parseInt(medio_movimiento));
        b.setJugada_completa(Integer.parseInt(jugada_completa));

        for (int r = 0; r < 8; r++)
            for (int f = 0; f < 8; f++)
                b.setPieza(r, f, tablero[r][f]);

        return b;
    }

    // Verificación de pertenencia y representación

    private static boolean pieza_valida(char c) {
        return "pnbrqkPNBRQK".indexOf(c) != -1;
    }

    private static boolean no_negativo (String s) {
        if (s == null || s.isEmpty()) return false;
        for (char ch : s.toCharArray()) if (!Character.isDigit(ch)) return false;
        return true;
    }

    private static boolean es_positivo(String s) {
        if (!no_negativo(s)) return false;
        return Integer.parseInt(s) >= 1;
    }
}
