package ativ01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int option;
        String fileName = "";

        do {
            System.out.println("1 - Small graph (100 vertices)");
            System.out.println("2 - Big graph (50000 vertices)");
            System.out.println("0 - Exit");
            System.out.print("Choose an option: ");
            option = sc.nextInt();
            switch (option) {
                case 1:
                    fileName = "ativ01/graph-test-100.txt";
                    break;
                case 2:
                    fileName = "ativ01/graph-test-50000.txt";
                    break;
                case 0:
                    break;
                default:
                    break;
            }
            Graph graph = readGraph(fileName);
            System.out.print("Select a vertice: ");
            int v = sc.nextInt(); // vertice
            printResults(graph, v);

        } while(option != 0);

        sc.close();
    }

    // gemini me ajudou nessa parte a fazer a leitura com bufferReader
    public static Graph readGraph(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String firstLine = reader.readLine();

            String[] parts = firstLine.trim().split("\\s+");

            int n = Integer.parseInt(parts[0]); // vértices
            int m = Integer.parseInt(parts[1]); // arestas

            Graph graph = new Graph(n);

            for (int i = 0; i < m; i++) {
                String edgeLine = reader.readLine();

                
                if (edgeLine == null || edgeLine.trim().isEmpty()) {
                    i--;
                    continue;
                }

                String[] edgeParts = edgeLine.trim().split("\\s+");
                int o = Integer.parseInt(edgeParts[0]); // origem
                int d = Integer.parseInt(edgeParts[1]); // destino

                graph.addEdge(o, d);
            }

            return graph;

        } catch (IOException e) { // FileNotFoundException é um tipo de IOException
            System.err.println("Error to read the file '" + fileName + "': " + e.getMessage());
            return null;
        }
    }

    public static void printResults(Graph graph, int v) {

        // grau saida = tam da lista de sucessores
        System.out.println("Exit degree: " + graph.getSucessors(v).size()); 

        // grau entrada = tam da lista de predecessores
        System.out.println("Entry degree: " + graph.getPredecessors(v).size());

        // sucessores
        System.out.println("Sucessors: " + graph.getSucessors(v));

        // predecessores
        System.out.println("Predecessors: " + graph.getPredecessors(v));
    }
}

class Graph {
    private int n; // num vertices
    private List<List<Integer>> sucessorList;
    private List<List<Integer>> predecessorList;

    public Graph(int n) {
        this.n = n;
        sucessorList = new ArrayList<>(n + 1);
        predecessorList = new ArrayList<>(n + 1);

        for (int i = 0; i <= n; i++) {
            sucessorList.add(new ArrayList<>());
            predecessorList.add(new ArrayList<>());
        }
    }

    public void addEdge(int o, int d) {
        sucessorList.get(o).add(d); // origem -> destino
        predecessorList.get(d).add(o); // destino -> origem
    }

    public List<Integer> getSucessors(int v) {
        return sucessorList.get(v);
    }

    public List<Integer> getPredecessors(int v) {
        return predecessorList.get(v);
    }
}
