#!/usr/bin/env python3
import curses, time, random, glob, pathlib, os

def find_iio_accel():
    for dev in glob.glob("/sys/bus/i2c/devices/i2c-5/new_device*"):
        namef = pathlib.Path(dev, "name")
        if namef.exists():
            n = namef.read_text().strip().lower()
            if any(k in n for k in ("accel","adxl","lis3","mpu","mma")):
                xr = pathlib.Path(dev, "in_accel_x_raw")
                yr = pathlib.Path(dev, "in_accel_y_raw")
                zr = pathlib.Path(dev, "in_accel_z_raw")
                sc = pathlib.Path(dev, "in_accel_scale")
                if xr.exists() and yr.exists() and zr.exists() and sc.exists():
                    return dev
    return None

class AccelReader:
    def __init__(self):
        self.dev = find_iio_accel()
        self.scale = 1.0
        if self.dev:
            try: self.scale = float(open(os.path.join(self.dev,"in_accel_scale")).read().strip())
            except: self.scale = 1.0
        self.fx = self.fy = self.fz = 0.0

    def ok(self): return self.dev is not None

    def read_ms2(self):
        if not self.dev: return (0.0,0.0,0.0)
        try:
            xr = int(open(os.path.join(self.dev,"in_accel_x_raw")).read().strip())
            yr = int(open(os.path.join(self.dev,"in_accel_y_raw")).read().strip())
            zr = int(open(os.path.join(self.dev,"in_accel_z_raw")).read().strip())
            x = xr*self.scale; y = yr*self.scale; z = zr*self.scale
            alpha = 0.25
            self.fx = alpha*x + (1-alpha)*self.fx
            self.fy = alpha*y + (1-alpha)*self.fy
            self.fz = alpha*z + (1-alpha)*self.fz
            return (self.fx, self.fy, self.fz)
        except:
            return (0.0,0.0,0.0)

DIRS = {"UP":(-1,0), "DOWN":(1,0), "LEFT":(0,-1), "RIGHT":(0,1)}

def accel_to_dir(ax, ay, deadzone=1.2):
    # Flip signs here if your mounting feels inverted
    # ax *= -1
    # ay *= +1
    if abs(ax) < deadzone and abs(ay) < deadzone:
        return None
    if abs(ax) >= abs(ay):
        return "DOWN" if ax > 0 else "UP"
    else:
        return "RIGHT" if ay > 0 else "LEFT"

def place_food(h, w, snake):
    while True:
        r = random.randint(1, h-2)
        c = random.randint(1, w-2)
        if (r,c) not in snake:
            return (r,c)

def main(stdscr):
    curses.curs_set(0); stdscr.nodelay(True); stdscr.timeout(100)
    sh, sw = stdscr.getmaxyx()
    h = min(24, sh-1); w = min(60, sw-1); top=0; left=0

    stdscr.clear()
    for x in range(left, left+w): stdscr.addch(top, x, "#"); stdscr.addch(top+h-1, x, "#")
    for y in range(top, top+h):   stdscr.addch(y, left, "#"); stdscr.addch(y, left+w-1, "#")

    snake = [(h//2, w//2+1), (h//2, w//2), (h//2, w//2-1)]
    direction = "RIGHT"; food = place_food(h, w, snake); score = 0
    accel = AccelReader(); last_turn = 0.0

    while True:
        if accel.ok():
            ax, ay, _ = accel.read_ms2()
            nd = accel_to_dir(ax, ay, deadzone=1.2)
            if nd and (direction, nd) not in {("UP","DOWN"),("DOWN","UP"),("LEFT","RIGHT"),("RIGHT","LEFT")}:
                if time.time() - last_turn > 0.08:
                    direction = nd; last_turn = time.time()

        key = stdscr.getch()
        km = {curses.KEY_UP:"UP", curses.KEY_DOWN:"DOWN", curses.KEY_LEFT:"LEFT", curses.KEY_RIGHT:"RIGHT",
              ord('w'):"UP", ord('s'):"DOWN", ord('a'):"LEFT", ord('d'):"RIGHT", ord('q'):"QUIT"}
        if key in km:
            if km[key] == "QUIT": return
            nd = km[key]
            if (direction, nd) not in {("UP","DOWN"),("DOWN","UP"),("LEFT","RIGHT"),("RIGHT","LEFT")}:
                direction = nd

        dr, dc = DIRS[direction]; head = (snake[0][0]+dr, snake[0][1]+dc)
        if head[0] in {top, top+h-1} or head[1] in {left, left+w-1} or head in snake:
            msg = f" Game Over! Score: {score}  (press any key) "
            stdscr.addstr(top + h//2, max(left+1, (w-len(msg))//2), msg)
            stdscr.nodelay(False); stdscr.getch(); return

        snake.insert(0, head)
        if head == food: score += 1; food = place_food(h, w, snake)
        else: snake.pop()

        for y in range(top+1, top+h-1): stdscr.addstr(y, left+1, " "*(w-2))
        stdscr.addch(snake[0][0], snake[0][1], "O")
        for seg in snake[1:]: stdscr.addch(seg[0], seg[1], "o")
        stdscr.addch(food[0], food[1], "*")
        status = f"Tilt to steer | Score:{score} | accel:{'OK' if accel.ok() else 'KEYS'} "
        stdscr.addstr(top, left+2, status[:w-4])
        stdscr.refresh(); time.sleep(0.03)

if __name__ == "__main__":
    curses.wrapper(main)

