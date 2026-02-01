import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class BogdanIonas { // <-- schimbă cu NumePrenume cerut

    // ======= CLASA Angajati (cerința 2 + 3) =======
    static class Angajati {
        private String angNume;
        private double tarifOrar;
        private int oreLucrate;
        private double avansSalariu;

        // constructor explicit cu 4 parametri
        public Angajati(String angNume, double tarifOrar, int oreLucrate, double avansSalariu) {
            this.angNume = angNume;
            this.tarifOrar = tarifOrar;
            this.oreLucrate = oreLucrate;
            this.avansSalariu = avansSalariu;
        }

        public String getNume() { return angNume; }
        public void setNume(String angNume) { this.angNume = angNume; }

        public double getTarif() { return tarifOrar; }
        public void setTarif(double tarifOrar) { this.tarifOrar = tarifOrar; }

        public int getOre() { return oreLucrate; }
        public void setOre(int oreLucrate) { this.oreLucrate = oreLucrate; }

        public double getAvans() { return avansSalariu; }
        public void setAvans(double avansSalariu) { this.avansSalariu = avansSalariu; }

        // info completă pentru afișare
        @Override
        public String toString() {
            double total = tarifOrar * oreLucrate;
            double dePlata = total - avansSalariu;
            return angNume + " | tarif=" + tarifOrar + " | ore=" + oreLucrate + " | avans=" + avansSalariu
                    + " | total=" + total + " | de_plata=" + dePlata;
        }
    }

    // ======= UI + LOGICA (cerințele 4-8) =======
    private Angajati curent;

    private DefaultListModel<Angajati> modelLista = new DefaultListModel<>();
    private JList<Angajati> lista = new JList<>(modelLista);

    private JTextField tfNume = new JTextField();
    private JTextField tfTarif = new JTextField();
    private JTextField tfOre = new JTextField();
    private JTextField tfAvans = new JTextField();

    // 5 etichete: 4 pentru nume/tarif/ore/avans + 1 pentru info completă
    private JLabel lblNume = new JLabel("Nume: -");
    private JLabel lblTarif = new JLabel("Tarif: -");
    private JLabel lblOre = new JLabel("Ore: -");
    private JLabel lblAvans = new JLabel("Avans: -");
    private JLabel lblInfo = new JLabel("Info completă: -");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BogdanIonas().porneste()); // <-- schimbă clasa dacă schimbi numele
    }

    private void porneste() {
        JFrame f = new JFrame("Gestionare Angajați");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 450);
        f.setLocationRelativeTo(null);

        // panouri
        JPanel stanga = new JPanel(new BorderLayout());
        JPanel dreapta = new JPanel();
        dreapta.setLayout(new BoxLayout(dreapta, BoxLayout.Y_AXIS));

        // listă
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stanga.add(new JScrollPane(lista), BorderLayout.CENTER);

        // adaugăm câteva obiecte inițiale ca să existe “la un moment dat”
        modelLista.addElement(new Angajati("Popescu Ion", 50.0, 160, 500.0));
        modelLista.addElement(new Angajati("Ionescu Maria", 60.0, 120, 200.0));
        lista.setSelectedIndex(0); // selectăm primul
        curent = modelLista.get(0);

        // câmpuri text (4)
        dreapta.add(new JLabel("Nume (angNume):"));
        dreapta.add(tfNume);
        dreapta.add(Box.createVerticalStrut(5));

        dreapta.add(new JLabel("Tarif orar (tarifOrar):"));
        dreapta.add(tfTarif);
        dreapta.add(Box.createVerticalStrut(5));

        dreapta.add(new JLabel("Ore lucrate (oreLucrate):"));
        dreapta.add(tfOre);
        dreapta.add(Box.createVerticalStrut(5));

        dreapta.add(new JLabel("Avans (avansSalariu):"));
        dreapta.add(tfAvans);
        dreapta.add(Box.createVerticalStrut(10));

        // etichete (4 + 1)
        dreapta.add(lblNume);
        dreapta.add(lblTarif);
        dreapta.add(lblOre);
        dreapta.add(lblAvans);
        dreapta.add(Box.createVerticalStrut(10));
        dreapta.add(lblInfo);

        // butoane (6)
        JPanel butoane = new JPanel(new GridLayout(2, 3, 8, 8));
        JButton bSetNume = new JButton("Set Nume");
        JButton bSetTarif = new JButton("Set Tarif");
        JButton bSetOre = new JButton("Set Ore");
        JButton bSetAvans = new JButton("Set Avans");
        JButton bAdaugaNou = new JButton("Creează obiect nou");
        JButton bAfiseazaInfo = new JButton("Afișează info completă");

        butoane.add(bSetNume);
        butoane.add(bSetTarif);
        butoane.add(bSetOre);
        butoane.add(bSetAvans);
        butoane.add(bAdaugaNou);
        butoane.add(bAfiseazaInfo);

        dreapta.add(Box.createVerticalStrut(10));
        dreapta.add(butoane);

        // layout principal
        f.setLayout(new GridLayout(1, 2));
        f.add(stanga);
        f.add(dreapta);

        // ======= evenimente =======

        // când selectezi din listă, actualizează “curent” + etichetele 4
        lista.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Angajati selectat = lista.getSelectedValue();
                    if (selectat != null) {
                        curent = selectat;
                        afiseazaEticheteScurte();
                    }
                }
            }
        });

        // primele 4 butoane: preiau din textfield și salvez în curent
        bSetNume.addActionListener(e -> {
            if (curent == null) return;
            curent.setNume(tfNume.getText().trim());
            afiseazaEticheteScurte();
            lista.repaint();
        });

        bSetTarif.addActionListener(e -> {
            if (curent == null) return;
            Double v = citesteDouble(tfTarif.getText().trim());
            if (v == null) {
                mesajEroare(f, "Tarif invalid. Exemplu: 50 sau 50.5");
                return;
            }
            curent.setTarif(v);
            afiseazaEticheteScurte();
            lista.repaint();
        });

        bSetOre.addActionListener(e -> {
            if (curent == null) return;
            Integer v = citesteInt(tfOre.getText().trim());
            if (v == null) {
                mesajEroare(f, "Ore invalide. Exemplu: 160");
                return;
            }
            curent.setOre(v);
            afiseazaEticheteScurte();
            lista.repaint();
        });

        bSetAvans.addActionListener(e -> {
            if (curent == null) return;
            Double v = citesteDouble(tfAvans.getText().trim());
            if (v == null) {
                mesajEroare(f, "Avans invalid. Exemplu: 200 sau 200.5");
                return;
            }
            curent.setAvans(v);
            afiseazaEticheteScurte();
            lista.repaint();
        });

        // buton 5: creează obiect nou (din câmpuri dacă sunt completate, altfel valori implicite)
        bAdaugaNou.addActionListener(e -> {
            String nume = tfNume.getText().trim();
            String sTarif = tfTarif.getText().trim();
            String sOre = tfOre.getText().trim();
            String sAvans = tfAvans.getText().trim();

            // valori implicite dacă sunt goale sau invalide
            if (nume.isEmpty()) nume = "Angajat Nou";

            Double tarif = citesteDouble(sTarif);
            if (tarif == null) tarif = 0.0;

            Integer ore = citesteInt(sOre);
            if (ore == null) ore = 0;

            Double avans = citesteDouble(sAvans);
            if (avans == null) avans = 0.0;

            Angajati nou = new Angajati(nume, tarif, ore, avans);
            modelLista.addElement(nou);
            lista.setSelectedIndex(modelLista.size() - 1); // selectăm noul obiect
            curent = nou;
            afiseazaEticheteScurte();
            lblInfo.setText("Info completă: -");
        });

        // buton 6: afișează info completă (toString) în ultima etichetă
        bAfiseazaInfo.addActionListener(e -> {
            if (curent == null) return;
            lblInfo.setText("Info completă: " + curent.toString());
        });

        // inițial
        afiseazaEticheteScurte();

        f.setVisible(true);
    }

    private void afiseazaEticheteScurte() {
        if (curent == null) {
            lblNume.setText("Nume: -");
            lblTarif.setText("Tarif: -");
            lblOre.setText("Ore: -");
            lblAvans.setText("Avans: -");
            return;
        }
        lblNume.setText("Nume: " + curent.getNume());
        lblTarif.setText("Tarif: " + curent.getTarif());
        lblOre.setText("Ore: " + curent.getOre());
        lblAvans.setText("Avans: " + curent.getAvans());
    }

    private static Double citesteDouble(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static Integer citesteInt(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static void mesajEroare(Component parent, String text) {
        JOptionPane.showMessageDialog(parent, text, "Eroare", JOptionPane.ERROR_MESSAGE);
    }
}
