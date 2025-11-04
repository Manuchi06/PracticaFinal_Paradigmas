
# Practica Final


### Estudiante: Manuela Buriticá Guzmán

---

##  Analizador sintáctico FEN con interfaz gráfica en Java (Ajedrez)

###  Objetivo General

Diseñar e implementar en el lenguaje de programación Java un programa que analice cadenas escritas en el formato FEN, validando su estructura sintáctica y mostrando la posición resultante en un tablero gráfico de ajedrez.


###  Objetivos Específicos

1. Implementar un analizador que verifique la validez de una cadena FEN.
2. Generar un mensaje de error cuando la cadena sea inválida.
3. Permitir al usuario mover piezas arrastrándolas con el mouse (drag & drop).
4. Generar en tiempo real la nueva cadena FEN según la posición actual.
5. Representar visualmente el tablero de ajedrez usando Swing e ilustraciones de piezas en formato PNG.
6. Diseñar una interfaz que permita al usuario ingresar, analizar y limpiar posiciones.


###  Estructura del código

El proyecto está compuesto por cuatro clases principales:

1. **FENParser:** Analiza la cadena FEN, validá su formato y construye un objeto Tablero.     
2. **Tablero:** Representa internamente el estado del juego (piezas, turno, enroque, etc.).   
3. **Panel_Control:** Dibuja el tablero y las piezas.   
4. **Main:** Crea la ventana principal, gestiona eventos y muestra los resultados.


### Funcionamiento del programa

1. Al iniciar, el tablero se muestra con la posición inicial predeterminada del ajedrez.
2. El usuario ingresa una cadena FEN en el campo de texto superior.
3. Al presionar el boton “Analizar jugada”, el programa:

   - Divide la cadena en 6 partes.
   - Verifica cada campo (colocación, turno, enroque, en passant, etc.).
   - Si la cadena es válida, construye el objeto Tablero y lo muestra gráficamente.
   - Si hay error, muestra un mensaje indicando la causa.
4. El botón “Limpiar” borra el tablero actual.
5. También es posible interactuar directamente con el tablero:
   - Puedes mover las piezas con el mouse (arrastrar y soltar).
   - Cada vez que realizas un movimiento, el programa actualiza automáticamente la cadena FEN en el campo de texto superior.


###  IDES utilizados

- **Lenguaje:** Java 25
- **Editor de texto:** IntelliJ IDEA
- **Interprete:** IntelliJ IDEA
- **Librerias graficas:** Swing, AWT, ImageIO


### Ejemplo de uso

1. **Entrada FEN válida:**
  - **Caso 1:** rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
  - **Caso 2:** r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 2 4

**Resultado esperado:**
  - **Ambos casos** El tablero muestra la posición del ajedrez con piezas personalizadas.
    
    "FEN analizado correctamente. Continue: "


2. **Entradas inválidas:**
  - **Caso 3:** 2r3k1/p3bqp1/Q2p3p/3Pp3/P3C3/8/5PPP/5RK1
  - **Caso 4:** rnbqkbnr/pppppppp/8/8/8/8/PPPPP/PPPPPPPP/RNBQKBNR w KQkq - 0 1

**Resultado esperado:**
  - **Caso 3:** “La cadena FEN debe tener 6 campos separados por espacios. Se encontraron: 1.”
  - **Caso 4:** "La colocación de piezas debe tener 8 filas separadas por '/'. Se encontró: 9"


