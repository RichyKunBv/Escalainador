/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package escalainador;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author rescamilla
 */
public class Main extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main.class.getName());

    // Constantes de la aplicación
    private static final String APP_VERSION = "0.2.0";
    private static final String APP_AUTHOR = "rescamilla";
    private static final String GIT_AUTHOR = "https://github.com/RichyKunBv";
    private static final String APP_GIT = "Aun no hay lol que flojo el creador";
    private static final String APP_ICON = "ayane.jpg"; 
    private static final String APP_NAME = "EscalaInador Super Full HD 4K LG TV+ HDR UHD Retina Pro";
    private static final String APP_NM = "EscalaInador";
    private static final String APP_CN = "Ayane";

    // Componentes adicionales no generados automáticamente
    private JLabel imageLabel;
    private BufferedImage originalImage;
    private final double[] zoomFactors = {0.05, 0.25, 0.5, 0.75, 1.0, 2.0, 3.0, 4.0, 5.0};
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        
        // Configuración inicial
        setTitle(APP_NAME);
        configurarIcono();
        configurarSlider();
        configurarAreaImagen();
        configurarReloj();
        
                // Asignar acciones a los menús
        jMenuOpen.addActionListener(e -> abrirImagen());
        jMenuExit.addActionListener(e -> salir());
        jMenuHelp.addActionListener(e -> mostrarAyuda());
        jMenuAD.addActionListener(e -> acercaDe());
        jMenuHV.addActionListener(e -> historialVersiones());
        // Exportar a SVG (pendiente de implementar, por ahora mensaje)
        jMenuExport.addActionListener(e -> JOptionPane.showMessageDialog(this, "Función no implementada aún."));

        // Toolbar no flotante
        jToolBar1.setFloatable(false);
        jLabelVersion.setText("Versión: " + APP_VERSION);

    }
    
        private void configurarIcono() {
        try {
            java.net.URL urlIcono = getClass().getResource("/escalainador/" + APP_ICON);
            if (urlIcono == null) {
                urlIcono = getClass().getResource(APP_ICON);
            }
            if (urlIcono != null) {
                java.awt.Image icono = new javax.swing.ImageIcon(urlIcono).getImage();
                setIconImage(icono);
                if (java.awt.Taskbar.isTaskbarSupported()) {
                    java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();
                    if (taskbar.isSupported(java.awt.Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(icono);
                    }
                }
            } else {
                System.err.println("¡No se encontró " + APP_ICON + "!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
        
    private void configurarSlider() {
        jSliderZoom.setMinimum(0);
        jSliderZoom.setMaximum(8);
        jSliderZoom.setValue(4); // posición central (1x)
        jSliderZoom.setMajorTickSpacing(1);
        jSliderZoom.setSnapToTicks(true);
        jSliderZoom.setPaintTicks(true);
        jSliderZoom.setPaintLabels(true);

        // Personalizar etiquetas del slider
        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
        // Las posiciones van de 0 a 8. Arriba (8) = 5.0, abajo (0) = 0.05
        labelTable.put(8, new JLabel("5x"));
        labelTable.put(7, new JLabel("4x"));
        labelTable.put(6, new JLabel("3x"));
        labelTable.put(5, new JLabel("2x"));
        labelTable.put(4, new JLabel("1x"));
        labelTable.put(3, new JLabel("0.75"));
        labelTable.put(2, new JLabel("0.5"));
        labelTable.put(1, new JLabel("0.25"));
        labelTable.put(0, new JLabel("0.05"));
        jSliderZoom.setLabelTable(labelTable);

        // Listener del slider
        jSliderZoom.addChangeListener(e -> actualizarZoom());
    }
    
    private void configurarAreaImagen() {
        // Crear un JLabel para mostrar la imagen dentro del JScrollPane
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        jScrollPane1.setViewportView(imageLabel);
    }

    private void configurarReloj() {
        // Crear etiqueta para el día (si no existe, la creamos dinámicamente)
        if (jLabelDate == null) {
            jLabelDate = new JLabel("Día: ");
            jToolBar1.add(jLabelDate, 0); // Insertar al principio
        }
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            jLabelDate.setText("Día: " + now.toLocalDate());
            jLabelHour.setText(" Hora: " + now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
        timer.start();
    }
    
    private void abrirImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes JPG", "jpg", "jpeg"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(file);
                actualizarZoom(); // mostrar con zoom actual
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen:\n" + ex.getMessage());
            }
        }
    }
    
    private void actualizarZoom() {
        if (originalImage == null) return;

        int pos = jSliderZoom.getValue();
        double factor = zoomFactors[pos];

        int newWidth = (int) (originalImage.getWidth() * factor);
        int newHeight = (int) (originalImage.getHeight() * factor);

        // Evitar dimensiones cero
        if (newWidth < 1) newWidth = 1;
        if (newHeight < 1) newHeight = 1;

        Image scaled = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
    }
    
    private void mostrarAyuda() {
        JOptionPane.showMessageDialog(this,
                APP_NM + " v" + APP_VERSION + "\n\n" +
                "INSTRUCCIONES DE USO:\n" +
                "1. Abrir una imagen JPG con Archivo > Abrir.\n" +
                "2. Usar el slider vertical para hacer zoom (5x a 0.05x).\n" +
                "3. El zoom se aplica automáticamente.\n" +
                "4. Para salir, Archivo > Salir.");
    }

    private void salir() {
        int confirmarSalida = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir de la aplicación?",
                "Salir",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmarSalida == JOptionPane.YES_OPTION) {
            this.dispose();
            System.exit(0);
        }
    }
    
    private void acercaDe() {
        JOptionPane.showMessageDialog(this,
                APP_NM + "\n" +
                "Versión: " + APP_VERSION + "\n" +
                "Desarrollado por: " + APP_AUTHOR + "\n" +
                "Git del desarrollador: " + GIT_AUTHOR + "\n" +
                "Git del proyecto: " + APP_GIT + "\n" +
                "Codename: " + APP_CN);
    }

    private void historialVersiones() {
        JOptionPane.showMessageDialog(this,
                APP_NM + " v" + APP_VERSION + "\n" +
                "Git del proyecto: " + APP_GIT + "\n" +
                "Codename: " + APP_CN + "\n" +
                "0.1.0: Versión inicial con zoom y slider\n" +
                "0.2.0: Ahora hay imagenes y una monita china de logo yupi");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSliderZoom = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        jToolBar1 = new javax.swing.JToolBar();
        jLabelDate = new javax.swing.JLabel();
        jLabelHour = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabelVersion = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuOpen = new javax.swing.JMenuItem();
        jMenuExport = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuAD = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuHV = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSliderZoom.setOrientation(javax.swing.JSlider.VERTICAL);

        jToolBar1.setRollover(true);

        jLabelDate.setText("Fecha: ");
        jToolBar1.add(jLabelDate);

        jLabelHour.setText(" Hora:  ");
        jToolBar1.add(jLabelHour);
        jToolBar1.add(jSeparator3);

        jLabelVersion.setText("Version: ");
        jToolBar1.add(jLabelVersion);

        jMenu1.setText("Archivo");

        jMenuOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/escalainador/document-open.png"))); // NOI18N
        jMenuOpen.setText("Abrir");
        jMenu1.add(jMenuOpen);

        jMenuExport.setText("Exportar a SVG");
        jMenu1.add(jMenuExport);
        jMenu1.add(jSeparator1);

        jMenuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/escalainador/exit.png"))); // NOI18N
        jMenuExit.setText("Salir");
        jMenu1.add(jMenuExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ayuda");

        jMenuAD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuAD.setText("Acerca De");
        jMenu2.add(jMenuAD);

        jMenuHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuHelp.setText("Ayuda");
        jMenu2.add(jMenuHelp);
        jMenu2.add(jSeparator2);

        jMenuHV.setText("Historial de Versiones");
        jMenu2.add(jMenuHV);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSliderZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSliderZoom, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Main().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelHour;
    private javax.swing.JLabel jLabelVersion;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuAD;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuExit;
    private javax.swing.JMenuItem jMenuExport;
    private javax.swing.JMenuItem jMenuHV;
    private javax.swing.JMenuItem jMenuHelp;
    private javax.swing.JMenuItem jMenuOpen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSlider jSliderZoom;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
