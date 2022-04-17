import types.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

    List<Triangle> lastTriangles = new ArrayList<>();
    Triangle startTri;

    int runs = 0, gen = 0;
    int size;
    int triHeight;
    int maxGen;

    public Main(int size, int maxGen){
        this.size = size;
        this.maxGen = maxGen;

        triHeight = (int) Math.sqrt(Math.pow(size, 2) - (Math.pow(size, 2)/4));

        startTri = new Triangle(
                new Vec2(0, triHeight),
                new Vec2(size/2, 0),
                new Vec2(size, triHeight)
        );
        lastTriangles.add(startTri);
    }

    public Dimension getPreferredSize() {
        return new Dimension(size, triHeight);
    }

    void tick(){
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        runs++;
        if(runs < 2){
            return;
        }else if(runs == 2){
            g.setColor(Color.black);
            g.fillPolygon(getPoints(0, startTri), getPoints(1, startTri), 3);
            return;
        }
        gen++;

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        List<Triangle> newTriangles = new ArrayList<>();

        if(gen < maxGen){
            for (Triangle tri : lastTriangles) {
                Vec2[] points = {
                        tri.p[0],
                        getMidVec(tri.p[0], tri.p[1]),
                        tri.p[1],
                        getMidVec(tri.p[1], tri.p[2]),
                        tri.p[2],
                        getMidVec(tri.p[2], tri.p[0])
                };

                newTriangles.add(new Triangle(points[0], points[1], points[5]));
                newTriangles.add(new Triangle(points[1], points[2], points[3]));
                newTriangles.add(new Triangle(points[5], points[3], points[4]));
            }
        }else{
            newTriangles = lastTriangles;
        }

        g.setColor(Color.black);
        for (Triangle tri : newTriangles) {
            g.fillPolygon(getPoints(0, tri), getPoints(1, tri), 3);
        }

        lastTriangles = newTriangles;
    }

    Vec2 getMidVec(Vec2 p1, Vec2 p2){
        return new Vec2((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    int[] getPoints(int axis, Triangle tri){
        int[] p = new int[tri.p.length];
        for(int i = 0; i < p.length; i++){
            p[i] = axis == 0 ? tri.p[i].x : tri.p[i].y;
        }
        return p;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int size = 0, maxGen = 0;
        boolean validInput = false;
        do {
            System.out.print("Enter triangle size: ");
            if (scan.hasNextInt()){
                size = scan.nextInt();
                validInput = true;
            } else
                System.out.println("Invalid input, try again.");
            scan.nextLine();
        } while (!validInput);

        validInput = false;
        do {
            System.out.print("Enter generation count: ");
            if (scan.hasNextInt()){
                maxGen = scan.nextInt();
                validInput = true;
            } else
                System.out.println("Invalid input, try again.");
            scan.nextLine();
        } while (!validInput);


        JFrame frame = new JFrame();
        Main main = new Main(size, maxGen);

        frame.setTitle("Sierpinski triangle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(main);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);

        while(true){
            main.tick();
            try{
                Thread.sleep(2000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}