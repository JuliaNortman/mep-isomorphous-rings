package ua.knu.csc.iss.ynortman.utils;

import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

public class FileUtils {
    public static String generateTable(RingInteger[][] data) {
        StringBuilder html = new StringBuilder("<table>\n");
        for (RingInteger[] row : data) {
            html.append("<tr>");
            for (RingInteger cell : row) {
                html.append("<td>").append(cell.getNumber()).append("</td>");
            }
            html.append("</tr>\n");
        }
        html.append("</table>");
        return html.toString();
    }

    public static String generateMatrix(RingInteger[][] data) {
        StringBuilder htmlBuilder = new StringBuilder();
//        htmlBuilder.append("<!DOCTYPE html>\n");
//        htmlBuilder.append("<html>\n");
//        htmlBuilder.append("<head>\n");
//        htmlBuilder.append("\t<title>Matrix-like Layout without Borders and Braces</title>\n");
        htmlBuilder.append("\t<style>\n");
        htmlBuilder.append("\t\ttable {\n");
        htmlBuilder.append("\t\t\tborder-collapse: collapse;\n");
        htmlBuilder.append("\t\t\tmargin: 20px auto;\n");
        htmlBuilder.append("\t\t}\n");
        htmlBuilder.append("\t\ttd {\n");
        htmlBuilder.append("\t\t\twidth: 50px;\n");
        htmlBuilder.append("\t\t\theight: 50px;\n");
        htmlBuilder.append("\t\t\ttext-align: center;\n");
        htmlBuilder.append("\t\t\tfont-size: 20px;\n");
        htmlBuilder.append("\t\t}\n");
        htmlBuilder.append("\t</style>\n");
//        htmlBuilder.append("</head>\n");
//        htmlBuilder.append("<body>\n");
        htmlBuilder.append("\t<table>\n");
        for (RingInteger[] row : data) {
            htmlBuilder.append("\t\t<tr>\n");
            for (RingInteger cell : row) {
                htmlBuilder.append("\t\t\t<td>").append(cell.getNumber()).append("</td>\n");
            }
            htmlBuilder.append("\t\t</tr>\n");
        }
        htmlBuilder.append("\t</table>\n");
//        htmlBuilder.append("</body>\n");
//        htmlBuilder.append("</html>");
        return htmlBuilder.toString();
    }

    public static String printSubstitutionRow(RingInteger[] substitution) {
        StringBuilder html = new StringBuilder();
        html.append("<div>Перестановка: <p>(");
        for (int i = 0; i < substitution.length; ++i) {
            html.append(substitution[i].getNumber());
            if(i != substitution.length -1) {
                html.append(",");
            }
        }
        html.append(")</p></div>");
        html.append("<br>");
        return html.toString();
    }

    public static String printGenGResult(RingInteger[] arr) {
        RingInteger[] header = new RingInteger[arr.length];
        for(int i = 0; i < arr.length; ++i) {
            header[i] = RingInteger.valueOf(i, arr[0].getM());
        }

        RingInteger[][] matrix = new RingInteger[2][arr.length];
        matrix[0] = header;
        matrix[1] = arr;
        return printMatrix(matrix, "Визначальний рядок:");

//        StringBuilder html = new StringBuilder();
//        html.append("<div>Визначальний рядок: <p>[");
//        for (int i = 0; i < arr.length; ++i) {
//            html.append(arr[i].getNumber());
//            if(i != arr.length -1) {
//                html.append(",");
//            }
//        }
//        html.append("]</p></div>");
//        html.append("<br>");
//        return html.toString();
    }

    public static String printMatrix(RingInteger[][] matrix, String text) {
        StringBuilder html = new StringBuilder();
        html.append("<p>");
        html.append(text);
        html.append("</p>");
        html.append("<div>");
        html.append(generateMatrix(matrix));
        html.append("</div>");
        html.append("<br>");
        return html.toString();
    }

    public static String printVector(RingInteger[] vector, String text) {
        StringBuilder html = new StringBuilder();
        html.append("<div>");
        html.append(text);
        html.append("<p>(");
        for (int i = 0; i < vector.length; ++i) {
            html.append(vector[i].getNumber());
            if(i != vector.length -1) {
                html.append(",");
            }
        }
        html.append(")</p></div>");
        html.append("<br>");
        return html.toString();
    }

    public static String generateHTMLTableWithHeader(RingInteger[] arr) {
        StringBuilder sb = new StringBuilder();
        int n = arr.length;

        // Generate table header
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<th></th>");
        for (int i = 0; i < n; i++) {
            sb.append("<th>").append(i).append("</th>");
        }
        sb.append("</tr>");

        // Generate table rows
        for (int i = 0; i < n; i++) {
            sb.append("<tr>");
            sb.append("<th>").append(i).append("</th>");
            for (int j = 0; j < n; j++) {
                int index = i * n + j;
                sb.append("<td>").append(arr[index].getNumber()).append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }

    public static String printTableWithHeaders(RingInteger[][] matrix) {

                int n = matrix[0].length;
                int m = matrix.length;

                StringBuilder htmlBuilder = new StringBuilder();
                htmlBuilder.append("\t<style>\n");
                htmlBuilder.append("\t\ttable {\n");
                htmlBuilder.append("\t\t\tborder-collapse: collapse;\n");
                htmlBuilder.append("\t\t\tmargin: 20px auto;\n");
                htmlBuilder.append("\t\t}\n");
                htmlBuilder.append("\t\tth, td {\n");
                htmlBuilder.append("\t\t\tborder: 1px solid black;\n");
                htmlBuilder.append("\t\t\tpadding: 10px;\n");
                htmlBuilder.append("\t\t\ttext-align: center;\n");
                htmlBuilder.append("\t\t}\n");
                htmlBuilder.append("\t</style>\n");
                htmlBuilder.append("\t<table>\n");
                // first row with numbers from 0 to n-1
                htmlBuilder.append("\t\t<tr>\n");
                htmlBuilder.append("\t\t\t<th></th>\n");
                for (int i = 0; i < n; i++) {
                    htmlBuilder.append("\t\t\t<th>").append(i).append("</th>\n");
                }
                htmlBuilder.append("\t\t</tr>\n");
                // rest of the rows with 0 to m-1 in first column and array values in remaining columns
                for (int i = 0; i < m; i++) {
                    htmlBuilder.append("\t\t<tr>\n");
                    htmlBuilder.append("\t\t\t<th>").append(i).append("</th>\n");
                    for (int j = 0; j < n; j++) {
                        htmlBuilder.append("\t\t\t<td>").append(matrix[i][j].getNumber()).append("</td>\n");
                    }
                    htmlBuilder.append("\t\t</tr>\n");
                }
                htmlBuilder.append("\t</table>\n");
                htmlBuilder.append("<br>");

                return htmlBuilder.toString();

    }

    public static String end() {
        return "<hr>";
    }

}
