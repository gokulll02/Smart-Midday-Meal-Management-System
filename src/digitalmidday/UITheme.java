package digitalmidday;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
public class UITheme {

    public static final Color BG_MAIN        = new Color(0xF4F7FB);
    public static final Color BG_CARD        = Color.WHITE;
    public static final Color BG_SIDEBAR     = new Color(0x1A2340);

    public static final Color PRIMARY        = new Color(0x1E6FD9);   
    public static final Color PRIMARY_DARK   = new Color(0x1558B0);
    public static final Color SUCCESS        = new Color(0x1DBF73);   
    public static final Color SUCCESS_DARK   = new Color(0x18A362);
    public static final Color DANGER         = new Color(0xE8384F);   
    public static final Color WARNING        = new Color(0xFF8C00);   
    public static final Color PURPLE         = new Color(0x6C5CE7);

    public static final Color TEXT_HEADING   = new Color(0x1A2340);
    public static final Color TEXT_BODY      = new Color(0x3C4A63);
    public static final Color TEXT_MUTED     = new Color(0x8A95A8);
    public static final Color BORDER         = new Color(0xDDE3EE);
    public static final Color ROW_STRIPE     = new Color(0xF0F4FB);
    public static final Color LOW_STOCK_BG   = new Color(0xFFF0F0);
    public static final Color LOW_STOCK_FG   = new Color(0xC0392B);

    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD,  20);
    public static final Font FONT_SUBTITLE   = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font FONT_LABEL      = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONT_BODY       = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_TABLE_HDR  = new Font("Segoe UI", Font.BOLD,  12);

    public static JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed()  ? bg.darker() :
                          getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        b.setFont(FONT_LABEL);
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton makePrimary(String text)  { return makeButton(text, PRIMARY);  }
    public static JButton makeSuccess(String text)  { return makeButton(text, SUCCESS);  }
    public static JButton makeDanger(String text)   { return makeButton(text, DANGER);   }
    public static JButton makeWarning(String text)  { return makeButton(text, WARNING);  }
    public static JButton makePurple(String text)   { return makeButton(text, PURPLE);   }
    public static JButton makeSecondary(String text){ return makeButton(text, new Color(0x6B7A99)); }

    public static JTextField makeField() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    public static JPasswordField makePasswordField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    private static void styleField(JComponent f) {
        f.setFont(FONT_BODY);
        f.setForeground(TEXT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER, 8),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        f.setBackground(Color.WHITE);
    }

    public static <T> JComboBox<T> makeCombo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setFont(FONT_BODY);
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER, 8),
            BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
        return cb;
    }

    public static JLabel makeHeading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_TITLE);
        l.setForeground(TEXT_HEADING);
        return l;
    }

    public static JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_BODY);
        return l;
    }

    public static JLabel makeMuted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    public static JLabel makeStatusLabel() {
        JLabel l = new JLabel("");
        l.setFont(FONT_BODY);
        return l;
    }
    public static void styleTable(JTable t) {
        t.setFont(FONT_BODY);
        t.setRowHeight(28);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(0xD0E4FF));
        t.setSelectionForeground(TEXT_HEADING);
        t.setBackground(BG_CARD);
        t.setFillsViewportHeight(true);
        t.setGridColor(BORDER);

        JTableHeader h = t.getTableHeader();
        h.setFont(FONT_TABLE_HDR);
        h.setBackground(new Color(0xEBF1FB));
        h.setForeground(TEXT_HEADING);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY));
        h.setPreferredSize(new Dimension(h.getWidth(), 32));

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl,v,sel,foc,row,col);
                if (!sel) c.setBackground(row % 2 == 0 ? BG_CARD : ROW_STRIPE);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }

    public static JScrollPane makeScrollPane(JTable t) {
        styleTable(t);
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new RoundedBorder(BORDER, 10));
        sp.getViewport().setBackground(BG_CARD);
        return sp;
    }

    public static JPanel makeCard() {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER, 12),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return p;
    }


    public static JPanel makeHeaderBar(String titleText, JButton rightBtn) {
        JPanel header = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0, BG_SIDEBAR, getWidth(),0, PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 60));
        header.setOpaque(false);

        JLabel title = new JLabel(titleText);
        title.setFont(FONT_TITLE);
        title.setForeground(Color.WHITE);
        title.setBounds(20, 15, 500, 30);
        header.add(title);

        if (rightBtn != null) {
            rightBtn.setBounds(0, 12, 110, 34);
            header.add(rightBtn);
        }
        return header;
    }

    // ─── Window setup ────────────────────────────────────────────────────────
    public static void setupFrame(JFrame f, int w, int h) {
        f.setSize(w, h);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(BG_MAIN);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // ─── Inner: rounded border ───────────────────────────────────────────────
    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        public RoundedBorder(Color c, int r) { this.color = c; this.radius = r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c){ return new Insets(radius/2,radius/2,radius/2,radius/2); }
        @Override public boolean isBorderOpaque(){ return false; }
    }

    // ─── Inner: gradient header panel ────────────────────────────────────────
    public static class GradientPanel extends JPanel {
        private final Color c1, c2;
        public GradientPanel(Color c1, Color c2) {
            this.c1=c1; this.c2=c2;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0,0,c1,getWidth(),0,c2));
            g2.fillRect(0,0,getWidth(),getHeight());
            super.paintComponent(g);
        }
    }
}