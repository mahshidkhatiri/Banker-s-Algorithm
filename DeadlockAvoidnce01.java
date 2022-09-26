package DeadlockAvoidnce01;

import javax.swing.*;
import java.util.*;
import java.lang.*;

public class DeadlockAvoidnce01 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String R = JOptionPane.showInputDialog("enter your resource");
        System.out.println(R);
        int[] Resource = StringToArray(R);
        String c = JOptionPane.showInputDialog("enter your claim matrix line by line");
        System.out.println(c);
        String[] splited = c.split(" ");
        int rc = Resource.length;
        int p = splited.length / rc;
        int[][] claim = new int[p][rc];
        int o = 0;
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < rc; j++) {

                claim[i][j] = Integer.parseInt(splited[o]);
                o++;
            }
        }

        int[][] allocation = new int[p][rc];
        int[][] ca = new int[p][rc];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < rc; j++) {
                allocation[i][j] = 0;
                ca[i][j] = claim[i][j] - allocation[i][j];
            }
        }

        int[] availabale = new int[rc];
        for (int j = 0; j < rc; j++) {
            availabale[j] = Resource[j];
        }

        boolean f = true;
        Queue<int[]> queue = new LinkedList<int[]>();

        while (f) {
            String message1 = "";
            message1 += "availabale :";
            for (int i = 0; i < rc; i++) {
                message1 += availabale[i];
                message1 += " ";
            }
            
             message1 += "\n";
            message1 += "allocation :";
            message1 += "\n";
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < rc; j++) {
                    message1 += allocation[i][j];
                    message1 += " ";

                }
                message1 += "\n";
            }message1 += "\n";
            message1 += "c-a :";
            message1 += "\n";
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < rc; j++) {
                    message1 += ca[i][j];
                    message1 += " ";

                }
                message1 += "\n";
            }
            JOptionPane.showMessageDialog(null, message1);
            String message = "";
            String reqpns = JOptionPane.showInputDialog("enter your process number");
            int reqpnn = Integer.parseInt(reqpns)+1;
            String newreq = JOptionPane.showInputDialog("enter your request");
            String[] splited1 = newreq.split(" ");
            int[] newrequest = new int[splited.length + 1];
            for (int i = 0; i < splited1.length + 1; i++) {
                if (i == 0) {
                    newrequest[i] = reqpnn;
                } else {
                    newrequest[i] = Integer.parseInt(splited1[i - 1]);
                }
            }

            int check = CheckReq(availabale, allocation, claim, ca, newrequest);
            if (check == 0) {
                message += " Error total request > claim \n";

            } else if (check == 1) {

                message += " Request > availabale \n";

                queue.add(newrequest);

            } else {

                if (Safe(availabale, p, allocation, claim, ca, newrequest)) {

                    message += "Safe \n";

                    boolean x = true;
                    for (int j = 0; j < rc; j++) {
                        availabale[j] -= newrequest[j + 1];
                        allocation[newrequest[0] - 1][j] += newrequest[j + 1];
                        ca[newrequest[0] - 1][j] = claim[newrequest[0] - 1][j] - allocation[newrequest[0] - 1][j];
                        if (ca[newrequest[0] - 1][j] != 0) {
                            x = false;
                        }
                    }
                    if (x) {
                        for (int j = 0; j < rc; j++) {
                            availabale[j] += allocation[newrequest[0] - 1][j];
                            allocation[newrequest[0] - 1][j] = 0;

                        }
                    }

                } else {
                    message = " not Safe \n";

                    queue.add(newrequest);

                }

            }
            if (!queue.isEmpty()) {
                int[] r1 = queue.element();
                boolean bool = rEqualQhead(newrequest, r1);
                if (!bool) {
                    for (int i = 0; i < queue.size(); i++) {
                        int[] r = queue.poll();
                        int check1 = CheckReq(availabale, allocation, claim, ca, r);
                        if (check1 == 0) {
                            message += " Suspend req : Error total request > claim \n";

                        } else if (check1 == 1) {
                            message += " Suspend Request > availabale \n";

                            queue.add(r);
                        } else {
                            if (Safe(availabale, p, allocation, claim, ca, r)) {
                                message += "Suspend request is Safe  \n";

                                boolean x = true;
                                for (int j = 0; j < rc; j++) {
                                    availabale[j] -= r[j + 1];
                                    allocation[r[0] - 1][j] += r[j + 1];
                                    ca[r[0] - 1][j] = claim[r[0] - 1][j] - allocation[r[0] - 1][j];
                                    if (ca[r[0] - 1][j] != 0) {
                                        x = false;
                                    }
                                }
                                if (x) {
                                    for (int j = 0; j < rc; j++) {
                                        availabale[j] += allocation[r[0] - 1][j];
                                        allocation[r[0] - 1][j] = 0;

                                    }
                                }

                            } else {
                                message += " Suspend request is Not Safe \n";

                                queue.add(r);
                            }

                        }
                    }
                }

            }

            JOptionPane.showMessageDialog(null, message);
        }

    }

    static boolean rEqualQhead(int[] r, int[] qh) {
        for (int i = 0; i < r.length; i++) {
            if (r[i] != qh[i]) {
                return false;
            }
        }
        return true;
    }

    static boolean Safe(int[] a, int p, int[][] al, int[][] c, int[][] ca, int[] nr) {
        int[] aa = new int[a.length];
        int[][] ala = new int[p][a.length];
        int[][] caa = new int[p][a.length];
        for (int j = 0; j < a.length; j++) {
            aa[j] = a[j];
        }
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < a.length; j++) {
                ala[i][j] = al[i][j];
                caa[i][j] = ca[i][j];

            }
        }

        boolean x = true;
        for (int j = 0; j < a.length; j++) {
            aa[j] -= nr[j + 1];
            ala[nr[0] - 1][j] += nr[j + 1];
            caa[nr[0] - 1][j] = c[nr[0] - 1][j] - ala[nr[0] - 1][j];

            if (caa[nr[0] - 1][j] != 0) {
                x = false;
            }
        }
        if (x) {
            for (int j = 0; j < a.length; j++) {
                aa[j] += ala[nr[0] - 1][j];
                ala[nr[0] - 1][j] = 0;
            }
        }

        for (int i = 0; i < p; i++) {
            boolean b = false;

            for (int j = 0; j < a.length; j++) {

                if (aa[j] < caa[i][j]) {
                    b = false;
                    j = a.length - 1;

                } else if (caa[i][j] != 0) {
                    b = true;
                }
            }
            if (b) {
                for (int l = 0; l < a.length; l++) {
                    caa[i][l] = 0;
                    aa[l] += ala[i][l];
                }
                i = -1;
            }
        }

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < a.length; j++) {

                if (caa[i][j] != 0) {

                    return false;
                }
            }
        }
        return true;
    }

    static int CheckReq(int[] a, int[][] al, int[][] c, int[][] ca, int[] nr) {
        boolean b0 = false;
        for (int j = 0; j < a.length; j++) {
            if (al[nr[0] - 1][j] + nr[j + 1] > c[nr[0] - 1][j]) {
                b0 = true;
            }
        }
        boolean b1 = false;
        for (int j = 0; j < a.length; j++) {
            if (nr[j + 1] > a[j]) {
                b1 = true;
            }
        }

        if (b0) {
            return 0;
        } else if (b1) {
            return 1;
        } else {
            return 2;
        }
    }

    static int[] StringToArray(String a) {
        String[] splited = a.split(" ");
        int s = splited.length;
        int[] f = new int[s];
        for (int i = 0; i < s; i++) {
            f[i] = Integer.parseInt(splited[i]);
        }

        return f;
    }

}
