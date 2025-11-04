package com.fenparser;

import java.util.regex.*;

public class FENParser {

    // Excepción personalizada
    public static class FENExcepcion extends Exception {
        public FENExcepcion(String mensaje) { super(mensaje); }
    }

    // Regex precompiladas para eficiencia
    private static final Pattern FEN_PATTERN = Pattern.compile(
        "^([prnbqkPRNBQK1-8]{1,8}/){7}[prnbqkPRNBQK1-8]{1,8}\\s[w|b]\\s(K?Q?k?q?|-)\\s(-|[a-h][36])\\s\\d+\\s\\d+$"
    );

    private static final Pattern ROW_PATTERN = Pattern.compile("^[prnbqkPRNBQK1-8]{1,8}$");
    private static final Pattern EN_PASSANT_PATTERN = Pattern.compile("^-|[a-h][36]$");
    private static final Pattern CASTLING_PATTERN = Pattern.compile("^-|[KQkq]+$");
    private static final Pattern SIDE_PATTERN = Pattern.compile("^[wb]$");

    public static Tablero parse(String fen) throws FENExcepcion {
        if (fen == null || fen.trim().isEmpty())
            throw new FENExcepcion("La cadena FEN está vacía o es nula.");

        fen = fen.trim();

        // ✅ Validación general con regex principal
        if (!FEN_PATTERN.matcher(fen).matches())
            throw new FENExcepcion("Formato general FEN inválido. Revise la estructura o los campos.");

        // Separamos por espacios (ya sabemos que hay 6 partes válidas)
        String[] partes = fen.split("\\s+");
        String colocacion = partes[0];
        String mover_lado = partes[1];
        String enroque = partes[2];
        String captura = partes[3];
        String medio_movimiento = partes[4];
        String jugada_completa = partes[5];

        // Validaciones individuales adicionales (opcional pero pedagógico)
        if (!SIDE_PATTERN.matcher(mover_lado).matches())
            throw new FENExcepcion("El lado que debe mover debe ser 'w' o 'b'.");

        if (!CASTLING_PATTERN.matcher(enroque).matches())
            throw new FENExcepcion("Campo de enroque inválido. Solo se permite KQkq o '-'.");

        if (!EN_PASSANT_PATTERN.matcher(captura).matches())
            throw new FENExcepcion("Campo de captura al paso inválido.");

        // Procesar el campo de colocación
        String[] filas = colocacion.split("/");
        if (filas.length != 8)
            throw new FENExcepcion("Debe haber exactamente 8 filas en el tablero.");

        char[][] tablero = new char[8][8];

        for (int r = 0; r < 8; r++) {
            String fila = filas[r];
            if (!ROW_PATTERN.matcher(fila).matches())
                throw new FENExcepcion("Fila inválida: contiene caracteres no permitidos.");

            int col = 0;
            for (char c : fila.toCharArray()) {
                if (Character.isDigit(c)) {
                    int empty = c - '0';
                    for (int i = 0; i < empty; i++) tablero[r][col++] = '.';
                } else {
                    tablero[r][col++] = c;
                }
            }
            if (col != 8)
                throw new FENExcepcion("Fila " + (8 - r) + " no tiene exactamente 8 casillas.");
        }

        // Crear el objeto Tablero con la información ya validada
        Tablero b = new Tablero();
        b.setMover_lado(mover_lado.charAt(0));
        b.setEnroque(enroque);
        b.setCaptura(captura);
        b.setMedio_movimiento(Integer.parseInt(medio_movimiento));
        b.setJugada_completa(Integer.parseInt(jugada_completa));

        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                b.setPieza(r, c, tablero[r][c]);

        return b;
    }
}
