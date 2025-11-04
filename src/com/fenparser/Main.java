package com.fenparser;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::Crear_mostrar);
    }

    // Crea e inicializa todos los componentes gráficos
    private static void Crear_mostrar() {
        JFrame frame = new JFrame("FEN Parser - Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(253, 224, 231));

        Panel_Control panel_control = new Panel_Control();
        frame.add(panel_control, BorderLayout.CENTER);

        JPanel control = new JPanel(new BorderLayout());
        control.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        control.setBackground(new Color(253, 224, 231));

        JTextField fenField = new JTextField("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        fenField.setFont(new Font("Consolas", Font.PLAIN, 15));
        fenField.setBorder(BorderFactory.createLineBorder(new Color(166, 98, 114), 1));
        control.add(fenField, BorderLayout.NORTH);
        // Callback para actualizar la cadena FEN en tiempo real
        panel_control.setActualizar_fen(() -> fenField.setText(panel_control.generar_FEN()));

        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 8));
        botones.setBackground(new Color(253, 224, 231));

        JButton Btn_analizar = new JButton("Analizar jugada");
        JButton Btn_limpiar = new JButton("Limpiar");

        Font boton_front = new Font("Segoe UI", Font.BOLD, 13);
        Color Color_principal = new Color(166, 98, 114);
        Color Color_hover = new Color(252, 196, 210);

        for (JButton btn : new JButton[]{Btn_analizar, Btn_limpiar}) {
            btn.setBackground(Color_principal);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(boton_front);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(Color_hover);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(Color_principal);
                }
            });
        }

        botones.add(Btn_analizar);
        botones.add(Btn_limpiar);
        control.add(botones, BorderLayout.CENTER);
        frame.add(control, BorderLayout.NORTH);

        JLabel estado = new JLabel("Ingresa un FEN y dale click a Analizar", SwingConstants.CENTER);
        estado.setForeground(new Color(166, 98, 114));
        estado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        estado.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(estado, BorderLayout.SOUTH);

        Btn_analizar.addActionListener(e -> {
            try {
                Tablero b = FENParser.parse(fenField.getText());
                panel_control.setTablero(b);
                estado.setText("FEN analizado correctamente. Continue: " + b.getMover_lado());
            } catch (FENParser.FENExcepcion ex) {
                estado.setText("FEN invalido: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        Btn_limpiar.addActionListener(e -> {
            try {
                String fen_inicial = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
                Tablero tablero_inicial = FENParser.parse(fen_inicial);
                panel_control.setTablero(tablero_inicial);
                fenField.setText(fen_inicial);
                estado.setText("Tablero restablecido a la posición inicial");
            } catch (FENParser.FENExcepcion ex) {
                estado.setText("Error al restablecer el tablero: " + ex.getMessage());
            }
        });

        try {
            Tablero inicializar_tablero = FENParser.parse(fenField.getText());
            panel_control.setTablero(inicializar_tablero);
        } catch (FENParser.FENExcepcion ex) {
            estado.setText("Error al cargar el FEN por defecto: " + ex.getMessage());
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
