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
      return Integer.compare(this.x + this.y, o.x + o.y);
    }

  }

  public static String createKey(int sx, int sy, int tx, int ty) {
    return "(" + sx + ", " + sy + ") -> " + "(" + tx + ", " + ty + ")";
  }

  public static void main(String[] args) throws FileNotFoundException {
    FileInputStream fin = new FileInputStream("most_least_distance/input.txt");

    Scanner sc = new Scanner(fin);

    int tc = sc.nextInt();

    String line = sc.nextLine();

    while (line.length() <= 0) {
      line = sc.nextLine();
    }

    for (int i = 0; i < tc; i++) {
      String[] inf = line.split(" ");
      int size = Integer.parseInt(inf[0]);
      int totalCase = Integer.parseInt(inf[1]);

      char[][] map = new char[size][size];

      Set<String> tmp = new HashSet<String>();
      for (int j = 0; j < totalCase; j++) {
        line = sc.nextLine();
        int[] positions = getPos(line.trim(), false);
        int x = positions[0];
        int y = positions[1];
        tmp.add(x + " " + y);
      }
      targets = tmp;

      Queue<String> temp = new PriorityQueue<String>();
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
            }

            for (int ii = 0; ii < directions.length; ii++) {

              int nextx = curr.x + directions[ii][0];
              int nexty = curr.y + directions[ii][1];
              if (nextx == targetx && nexty == targety) {

                mapRes.put(key, curr.count + 1);
                found = true;
                break;
              }
              if (nextx >= 0 && nextx < map.length
                  && nexty >= 0 && nexty < map.length && map[nextx][nexty] == '1') {
                if (!visited[nextx][nexty]) {
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
      }

      for (Map.Entry<String, Integer> entry : mapRes.entrySet()) {
        System.out.println("Key = " + entry.getKey() +
            ", Value = " + entry.getValue());
      }

      System.out.println(mapRes);
    }

    sc.close();
  }
}