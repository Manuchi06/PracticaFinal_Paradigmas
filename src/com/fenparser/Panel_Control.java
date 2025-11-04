package com.fenparser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Panel_Control extends JPanel {
    private Tablero tablero;
    private Map<Character, Image> piezas_imagenes = new HashMap<>();
    private static final int SQUARE_SIZE = 64;
    private Point origen = null;
    private char pieza_seleccionada = '.';
    private Runnable actualizar_fen; // Actualiza la cadena FEN

    public Panel_Control() {
        setPreferredSize(new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8));
        cargar_imagen();
        agregar_listeners();
    }

    public void setActualizar_fen(Runnable callback) {
        this.actualizar_fen = callback;
    }

    public void setTablero(Tablero b) {
        this.tablero = b;
        repaint();
    }

    private void agregar_listeners() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (tablero == null) return;
                int columna = e.getX() / SQUARE_SIZE;
                int fila = e.getY() / SQUARE_SIZE;
                pieza_seleccionada = tablero.getPieza(fila, columna);
                if (pieza_seleccionada != '.' && pieza_seleccionada != '\0') {
                    origen = new Point(columna, fila);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (tablero == null || origen == null) return;
                int columna = e.getX() / SQUARE_SIZE;
                int fila = e.getY() / SQUARE_SIZE;

                // Move la pieza
                tablero.setPieza(origen.y, origen.x, '.');
                tablero.setPieza(fila, columna, pieza_seleccionada);

                origen = null;
                pieza_seleccionada = '.';
                repaint();

                // Actualiza FEN en la interfaz principal
                if (actualizar_fen != null) actualizar_fen.run();
            }
        });
    }

    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                boolean claro = (r + c) % 2 == 0;
                g2.setColor(claro ? new Color(255, 255, 255) : new Color(166, 98, 114));
                g2.fillRect(c * SQUARE_SIZE, r * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

                if (tablero != null) {
                    char p = tablero.getPieza(r, c);
                    if (p != '.' && p != '\0') {
                        Image img = piezas_imagenes.get(p);
                        if (img != null) {
                            g2.drawImage(img, c * SQUARE_SIZE, r * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
                        }
                    }
                }
            }
        }
        g2.dispose();
    }

    private void cargar_imagen() {
        Map<Character, String> nombres = Map.ofEntries(
                // Piezas blancas
                Map.entry('K', "Rey_rosa.png"),
                Map.entry('Q', "Reina_rosa.png"),
                Map.entry('B', "Alfil_rosa.png"),
                Map.entry('N', "Caballo_rosa.png"),
                Map.entry('R', "Torre_rosa.png"),
                Map.entry('P', "Peon_rosa.png"),

                // Piezas negras
                Map.entry('k', "Rey_negro.png"),
                Map.entry('q', "Reina_negra.png"),
                Map.entry('b', "Alfil_negro.png"),
                Map.entry('n', "Caballo_negro.png"),
                Map.entry('r', "Torre_negra.png"),
                Map.entry('p', "Peon_negro.png")
        );

        for (Map.Entry<Character, String> e : nombres.entrySet()) {
            try {
                BufferedImage img = ImageIO.read(getClass().getResource("/images/" + e.getValue()));
                if (img != null) piezas_imagenes.put(e.getKey(), img);
            } catch (Exception ex) {
            }
        }
    }

    // Genera la cadena FEN según la posición actual
    public String generar_FEN() {
        if (tablero == null) return "";

        StringBuilder fen = new StringBuilder();
        for (int r = 0; r < 8; r++) {
            int vacias = 0;
            for (int c = 0; c < 8; c++) {
                char p = tablero.getPieza(r, c);
                if (p == '.' || p == '\0') {
                    vacias++;
                } else {
                    if (vacias > 0) {
                        fen.append(vacias);
                        vacias = 0;
                    }
                    fen.append(p);
                }
            }
            if (vacias > 0) fen.append(vacias);
            if (r < 7) fen.append('/');
        }
        fen.append(" w - - 0 1");
        return fen.toString();
    }
}
