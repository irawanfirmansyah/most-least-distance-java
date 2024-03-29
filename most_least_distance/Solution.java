package most_least_distance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

class Solution {

  private static Set<String> targets;
  private static Queue<String> startingPoints;
  private static int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, };

  static int[] getPos(String loc, boolean ignoreDeduction) {
    int[] res = new int[2];
    String[] temp = loc.split(" ");
    res[0] = Integer.parseInt(temp[0]);
    res[1] = Integer.parseInt(temp[1]);
    if (!ignoreDeduction) {
      res[0] -= 1;
      res[1] -= 1;
    }
    return res;
  }

  static class Point implements Comparable<Point> {
    int sx;
    int sy;
    int x;
    int y;
    int count;
    Integer X;
    Integer Y;

    @Override
    public int compareTo(Point o) {
      return this.count - o.count;
    }

  }

  public static String createKey(int sx, int sy, int tx, int ty) {
    return sx + "," + sy + " -> " + tx + "," + ty;
  }

  public static void main(String[] args) throws FileNotFoundException {
    FileInputStream fin = new FileInputStream("most_least_distance/input.txt");

    Scanner sc = new Scanner(fin);

    int tc = Integer.parseInt(sc.nextLine());

    for (int i = 0; i < tc; i++) {
      String line = sc.nextLine();

      String[] inf = line.split(" ");
      int size = Integer.parseInt(inf[0]);
      int totalCase = Integer.parseInt(inf[1]);

      Set<String> tmp = new HashSet<String>();
      for (int j = 0; j < totalCase; j++) {
        String strTc = sc.nextLine();
        int[] positions = getPos(strTc.trim(), false);
        int x = positions[0];
        int y = positions[1];
        tmp.add(x + " " + y);
      }
      targets = tmp;

      Queue<String> temp = new PriorityQueue<String>();
      char[][] map = new char[size][size];

      for (int k = 0; k < size; k++) {
        String[] rows = sc.nextLine().split(" ");
        for (int kk = 0; kk < size; kk++) {
          String cur = k + " " + kk;
          char cha = rows[kk].charAt(0);
          if (!targets.contains(cur) && cha == '1') {
            temp.add(cur);
          }
          map[k][kk] = rows[kk].charAt(0);
        }
      }
      startingPoints = temp;

      Map<String, Integer> mapRes = new HashMap<String, Integer>();

      while (!startingPoints.isEmpty()) {
        String strtLoc = startingPoints.remove();
        int[] strtPos = getPos(strtLoc, true);
        int startx = strtPos[0];
        int starty = strtPos[1];

        Point strPoint = new Point();
        strPoint.count = 0;
        strPoint.sx = startx;
        strPoint.sy = starty;
        strPoint.x = startx;
        strPoint.y = starty;

        for (String target : targets) {
          int[] targetLoc = getPos(target, true);
          int targetx = targetLoc[0];
          int targety = targetLoc[1];

          String key = createKey(startx, starty, targetx, targety);

          Queue<Point> q = new PriorityQueue<Point>();
          boolean[][] visited = new boolean[size][size];
          q.add(strPoint);

          boolean found = false;
          while (!q.isEmpty() && !found) {
            Point curr = q.remove();
            if (!visited[curr.x][curr.y]) {
              visited[curr.x][curr.y] = true;
              for (int ii = 0; ii < directions.length; ii++) {

                int nextx = curr.x + directions[ii][0];
                int nexty = curr.y + directions[ii][1];

                if (nextx < 0 || nextx >= map.length
                    || nexty < 0 || nexty >= map.length)
                  continue;

                else if (map[nextx][nexty] != '1')
                  continue;

                else if (nextx == targetx && nexty == targety) {
                  // Using this to debug
                  // if (key.contains("2,3")) {

                  //   System.out.println(key);
                  //   System.out.println(curr.count + 1);
                  // }
                  mapRes.put(key, curr.count + 1);
                  found = true;
                  break;
                }

                Point np = new Point();
                np.count = curr.count + 1;
                np.x = nextx;
                np.y = nexty;
                np.sx = curr.sx;
                np.sy = curr.sy;
                q.add(np);
              }
            }
          }

        }
      }

      Map<String, Map<String, Integer>> group = new HashMap<String, Map<String, Integer>>();

      for (Map.Entry<String, Integer> entry : mapRes.entrySet()) {

        String[] sk = entry.getKey().split(" -> ");
        String sp = sk[0];
        String tp = sk[1];
        if (group.containsKey(sp)) {
          group.get(sp).put(tp, entry.getValue());
        } else {
          Map<String, Integer> tem = new HashMap<String, Integer>();
          tem.put(tp, entry.getValue());
          group.put(sp, tem);
        }
      }

      int smallest = 0;
      String smallestLoc = "";
      for (Map.Entry<String, Map<String, Integer>> entry : group.entrySet()) {
        int max = 0;
        for (Map.Entry<String, Integer> ent : entry.getValue().entrySet()) {
          if (max == 0) {
            max = ent.getValue();
          } else if (ent.getValue() > max) {
            max = ent.getValue();
          }
        }

        if (smallest == 0) {
          smallest = max;
          smallestLoc = entry.getKey();
        } else if (max < smallest) {
          smallest = max;
          smallestLoc = entry.getKey();
        }
      }
      System.out.println(smallest);
      System.out.println(smallestLoc);

    }

    sc.close();
  }
}