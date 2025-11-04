package com.fenparser;

public class Tablero {
    private char[][] tablero = new char[8][8];
    private char mover_lado;
    private String enroque;
    private String captura;
    private int medio_movimiento;
    private int jugada_completa;

    public Tablero() {
        // Inicializa todas las casillas como vacías
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                tablero[r][c] = '.';
        enroque = "-";
        captura = "-";
    }

    // Métodos de acceso (getters y setters)
    public char getPieza(int fila, int columna) { return tablero[fila][columna]; }
    public void setPieza(int fila, int columna, char p) { tablero[fila][columna] = p; }

    public char getMover_lado() { return mover_lado; }
    public void setMover_lado(char s) { mover_lado = s; }

    public String getEnroque() { return enroque; }
    public void setEnroque(String c) { enroque = c; }

    public String getCaptura() { return captura; }
    public void setCaptura(String e) { captura = e; }

    public int getMedio_movimiento() { return medio_movimiento; }
    public void setMedio_movimiento(int v) { medio_movimiento = v; }

    public int getJugada_completa() { return jugada_completa; }
    public void setJugada_completa(int v) { jugada_completa = v; }
}
