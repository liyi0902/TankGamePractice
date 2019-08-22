/*
 *   坦克游戏4
 *  1.画出坦克
 *  2.̹坦克可以上下左右移动
 *  3.可以发射子弹，子弹可以连发
 *  4.敌方坦克碰到子弹会消失
 *  5.敌人坦克也可以自由移动，但是不能超过边界
 *  6.敌人坦克也可以发射
 *  7.敌人坦克碰到我方坦克，我方坦克也会爆炸
 *  8.解决第一次爆炸效果不明显问题==>失败，待解决
 */
package TankGame4;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class TankGame4 extends JFrame{
	MyPanel mp=null;
	public static void main(String[] args) {
		TankGame4 tg=new TankGame4();
	}
	public TankGame4() {
		mp=new MyPanel();
		Thread t=new Thread(mp);
		t.start();
		this.add(mp);
		this.addKeyListener(mp);
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}

class MyPanel extends JPanel implements KeyListener,Runnable{
	//定义一个我的坦克
	Hero hero=null;
	//定义敌人的坦克组
	Vector<Enemy> enemis=new Vector<Enemy>();
	
	//定义炸弹的集合
	Vector<Bomb> bombs=new Vector<Bomb>();
	int enemySize=3;
	//定义三张用于爆炸效果的图片
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//构造函数
	public MyPanel() {
		hero=new Hero(200, 500);
		//初始化敌人坦克
		for(int i=0;i<enemySize;i++) {
			Enemy et=new Enemy((i+1)*50, 20);
			et.setColor(1);
			et.setDirection(2);
			Thread t=new Thread(et);
			t.start();
			//给敌人坦克添加一颗子弹
			Bullet b_enemy=new Bullet(et.x+5, et.y+20, 2);
			et.bb.add(b_enemy);
			Thread t2=new Thread(b_enemy);
			t2.start();
			enemis.add(et);
		}
//		try {
//			image1=ImageIO.read(new File("/bomb1.png"));
//			image2=ImageIO.read(new File("/bomb2.png"));
//			image3=ImageIO.read(new File("/bomb3.png"));
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		//初始化图片
		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb1.png"));
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb2.png"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb3.png"));
	}
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 800, 600);
		//画出坦克
		if(hero.isLive) {
			this.generateTank(hero.getX(), hero.getY(), g, hero.getDirection(), 0);
		}
		
		//遍历画出每颗子弹
		for(int i=0;i<hero.bb.size();i++) {
			Bullet b=hero.bb.get(i);
			//画出子弹，只画了一颗子弹
			if(b!=null&&b.isLive==true) {
				g.draw3DRect(b.x, b.y, 1, 1, false);
			}
			//当子弹死亡时，从Vector中删除该子弹
			if(b.isLive==false) {
				hero.bb.remove(b);
			}
		}
		//画出炸弹
		for(int i=0;i<bombs.size();i++) {
			Bomb bomb=bombs.get(i);
			if(bomb.life>6) {
				g.drawImage(image3, bomb.x, bomb.y, 30, 30, this);
			}else if(bomb.life>3) {
				g.drawImage(image2, bomb.x, bomb.y, 30, 30, this);
			}else {
				g.drawImage(image1, bomb.x, bomb.y, 30, 30, this);
			}
			//让生命值减少
			bomb.lifeDown();
			
			if(bomb.life==0) {
				bombs.remove(bomb);
			}
		}
		
		//画出敌人坦克
		for(int i=0;i<enemis.size();i++) {
			Enemy e=enemis.get(i);
			if(e.isLive) {
				this.generateTank(e.getX(), e.getY(), g, e.getDirection(), e.getColor());
				//画出敌人的子弹
				for(int j=0;j<e.bb.size();j++) {
					//取出子弹
					Bullet b_enemy=e.bb.get(j);
					if(b_enemy.isLive) {
						g.draw3DRect(b_enemy.x, b_enemy.y, 1, 1, false);
					}else {
						//如果敌人坦克死亡了，就从向量中删除子弹
						e.bb.remove(b_enemy);
					}
				}
			}
			
		}
	}
	//生成坦克的函数
	public void generateTank(int x, int y, Graphics g, int direction, int type) {
		//判断坦克的类型
		switch(type) {           	
		case 0:
			g.setColor(Color.yellow);
			break;
		case 1:
			g.setColor(Color.cyan);
			break;
		}
		//判断坦克的方向
		switch(direction) {
		case 0:
			g.fill3DRect(x-5, y-10, 5, 30, false);
			g.fill3DRect(x+10, y-10, 5, 30, false);
			g.fill3DRect(x, y-5, 10, 20, false);
			g.fillOval(x, y, 10, 10); 
			g.drawLine(x+5, y+5, x+5, y-10);
			break;
		case 1:
			g.fill3DRect(x-10, y-5, 30, 5, false);
			g.fill3DRect(x-10, y+10, 30, 5, false);
			g.fill3DRect(x-5, y, 20, 10, false);
			g.fillOval(x, y, 10, 10);
			g.drawLine(x+5, y+5, x+20, y+5);
			break;
		case 2:
			g.fill3DRect(x-5, y-10, 5, 30, false);
			g.fill3DRect(x+10, y-10, 5, 30, false);
			g.fill3DRect(x, y-5, 10, 20, false);
			g.fillOval(x, y, 10, 10);
			g.drawLine(x+5, y+5, x+5, y+20);
			break;
		case 3:
			g.fill3DRect(x-10, y-5, 30, 5, false);
			g.fill3DRect(x-10, y+10, 30, 5, false);
			g.fill3DRect(x-5, y, 20, 10, false);
			g.fillOval(x, y, 10, 10);
			g.drawLine(x+5, y+5, x-10, y+5);
			break;
		}
	}
	
	//写一个方法来判断我的子弹是否击中敌人坦克
	public void isHitEnemy() {
		//判断子弹是否击中
		for(int i=0;i<hero.bb.size();i++) {
			//取出子弹
			Bullet b=hero.bb.get(i);
			//判断子弹是否有效
			if(b.isLive) {
				for(int j=0;j<enemis.size();j++) {
					Enemy e=enemis.get(j);
					//判断坦克是否活着
					if(e.isLive) {
						this.isHitTank(b, e);
					}
				}
			}
		}
	}
	
	//判断敌人的坦克是否击中我
	public void isHitHero() {
		//取出每一个敌人坦克
		for(int i=0;i<enemis.size();i++) {
			Enemy e=enemis.get(i);
			for(int j=0;j<e.bb.size();j++) {
				Bullet b=e.bb.get(j);
				this.isHitTank(b, hero);
			}
		}
	}
	
	//写一个方法专门来判断是否击中坦克
	public void isHitTank(Bullet b, Tank e) {
		switch(e.getDirection()) {
		case 0:
		case 2:
			if(b.x>=e.getX()-5&&b.x<=e.getX()+15&&b.y>=e.getY()-10&&b.y<=e.getY()+20) {
				b.isLive=false;
				e.isLive=false;
				//创建一颗炸弹，放入向量中
				Bomb bomb=new Bomb(e.getX()-5, e.getY()-10);
				bombs.add(bomb);
			}
			break;
		case 1:
		case 3:
			if(b.x>=e.getX()-10&&b.x<=e.getX()+20&&b.y>=e.getY()-5&&b.y<=e.getY()+15) {
				b.isLive=false;
				e.isLive=false;
				Bomb bomb=new Bomb(e.getX()-5, e.getY()-10);
				bombs.add(bomb);
		}
			break;
		}
//		if(e.getDirection()==0||e.getDirection()==2) {
//			//e.getX()-5,e.getY()-10; e.getX()+15, e.getY()+20
//			//b.x,b.y
//			if(b.x>=e.getX()-5&&b.x<=e.getX()+15&&b.y>=e.getY()-10&&b.y<=e.getY()+20) {
//				
//			}
//		}else {
//			//e.x-10,e.y-5;e.x+20,e.y+15
//			if(b.x>=e.getX()-10&&b.x<=e.getX()+20&&b.y>=e.getY()-5&&b.y<=e.getY()+15) {
//				 	
//			}
//		}
	}
	//判断敌人坦克是否互相碰撞
	public void isCrashEnemy() {
		for(int i=0; i<enemis.size();i++) {
			Enemy enemyA=enemis.get(i);
			if(enemyA.isLive) {
				for(int j=0;j<enemis.size();j++) {
					if(i!=j) {
						Enemy enemyB=enemis.get(j);
						if(enemyB.isLive) {
							if(isCrash(enemyA, enemyB)) {
								enemyA.setDirection((int)Math.random()*4);
							}
						}
					}
				}
			}
		}
	}
	
	//判断敌人坦克是否撞到我的坦克
	public void isCrashHero() {
		for(int i=0;i<enemis.size();i++) {
			Enemy enemy=enemis.get(i);
			if(enemy.isLive) {
				if(isCrash(enemy, hero)) {
					enemy.setDirection((int)Math.random()*4);
				}
			}
		}
	}
	
	//judge whether the hero will crash with other tanks
	public boolean isHeroCrashEnemy() {
		for(int i=0;i<enemis.size();i++) {
			Enemy enemy=enemis.get(i);
			if(enemy.isLive) {
				if(isCrash(hero, enemy)) {
					return true;
				}
			}
		}
		return false;
	}
	
	//写一个方法专门判断两个坦克是否相撞
	public boolean isCrash(Tank a, Tank b) {
		switch(a.getDirection()) {
		case 0:
			//只有在a坦克上方的坦克可能被a坦克碰撞
			if(a.getY()>b.getY()) {
				switch(b.getDirection()) {
				//方向相同的坦克不会被撞到
				case 0:
					if(a.getY()-b.getY()<=35) {
						//if there will be a crash, return true
						return true;
						//a.setDirection((int)Math.random()*4);
					}
					break;
				//相向而行时，只考虑占用了“同一条路”的坦克
				case 2:
					if(((a.getX()>b.getX())&&(a.getX()-b.getX()<=10))||((a.getX()<b.getX())&&(b.getX()-a.getX()<=30))) {
						if(a.getY()-b.getY()<25) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 1:
					if((a.getX()>b.getX()&&(a.getX()-b.getX()<=15))||(a.getX()<b.getX()&&b.getX()-a.getX()<=35)) {
						if(a.getY()-b.getY()<25) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 3:
					if((a.getX()>b.getX()&&(a.getX()-b.getX()<=25))||(a.getX()<b.getX()&&b.getX()-a.getX()<=25)) {
						if(a.getY()-b.getY()<20) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				}
			}
			break;
		case 1:
			if(a.getX()<b.getX()) {
				switch(b.getDirection()) {
				case 0:
					if((a.getY()<b.getY()&&b.getY()-a.getY()<=25)||(a.getY()>b.getY()&&a.getY()-b.getY()<=25)) {
						if(b.getX()-a.getX()<20) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 1:
					if(a.getX()-b.getX()<=35) {
						return true;
						//a.setDirection((int)Math.random()*4);
					}
					break;
				case 2:
					if((a.getY()<b.getY()&&b.getY()-a.getY()<=35)||(a.getY()>b.getY()&&a.getY()-b.getY()<=15)) {
						if(b.getX()-a.getX()<20) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 3:
					if((a.getY()<b.getY()&&b.getY()-a.getY()<=30)||(a.getY()>b.getY()&&a.getY()-b.getY()<=10)) {
						if(b.getX()-a.getX()<20) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				}
			}
			break;
		case 2:
			if(a.getY()<b.getY()) {
				switch(b.getDirection()) {
				case 0:
					if((a.getX()<b.getX()&&b.getX()-a.getX()<=10)||(a.getX()>b.getX()&&a.getX()-b.getX()<=30)) {
						if(b.getY()-a.getY()<25) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 1:
					if((a.getX()<b.getX()&&b.getX()-a.getX()<=25)||(a.getX()>b.getX()&&a.getX()-b.getX()<=25)) {
						if(b.getY()-a.getY()<20) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 2:
					if(b.getY()-a.getY()<=35) {
						return true;
						//a.setDirection((int)Math.random()*4);
					}
					break;
				case 3:
					if((a.getX()<b.getX()&&b.getX()-a.getX()<=15)||(a.getX()>b.getX()&&a.getX()-b.getX()<=35)) {
						if(b.getY()-a.getY()<25) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				}
			}
			break;
		case 3:
			if(a.getX()>b.getX()) {
				switch(b.getDirection()) {
				case 0:
					if((a.getY()>b.getY()&&a.getY()-b.getY()<=35)||(a.getY()<b.getY()&&b.getY()-a.getY()<=15)) {
						if(a.getX()-b.getX()<30) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 1:
					if((a.getY()>b.getY()&&a.getY()-b.getY()<=30)||(a.getY()<b.getY()&&b.getY()-a.getY()<=10)) {
						if(a.getX()-b.getX()<25) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 2:
					if((a.getY()>b.getY()&&a.getY()-b.getY()<=25)||(a.getY()<b.getY()&&b.getY()-a.getY()<=25)) {
						if(a.getX()-b.getX()<20) {
							return true;
							//a.setDirection((int)Math.random()*4);
						}
					}
					break;
				case 3:
					if(b.getX()-a.getX()<=35) {
						return true;
						//a.setDirection((int)Math.random()*4);
					}
					break;
				}
			}
			break;
		}
		return false;
	}
	
	//随机生成出了现有方向别的方向
//	public int newDirection(int oldDirection) {
//		int newDirection;
//		while(true) {
//			newDirection=(int)Math.random()*4;
//			if(newDirection!=oldDirection) {
//				break;
//			}
//		}
//		return newDirection;
//	}
	
	@Override
	//w,s,a,d来控制
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()==KeyEvent.VK_W) {
			//设置坦克的方向
			this.hero.setDirection(0);
			if(isHeroCrashEnemy()) {
				
			}else {
				this.hero.moveUp();
			}
		}else if(arg0.getKeyCode()==KeyEvent.VK_D) {
			this.hero.setDirection(1);
			if(isHeroCrashEnemy()) {
				
			}else {
				this.hero.moveRight();
			}
		}else if(arg0.getKeyCode()==KeyEvent.VK_S) {
			this.hero.setDirection(2);
			if(isHeroCrashEnemy()) {
				
			}else {
				this.hero.moveDown();
			}
		}else if(arg0.getKeyCode()==KeyEvent.VK_A) {
			this.hero.setDirection(3);
			if(isHeroCrashEnemy()) {
				
			}else {
				this.hero.moveLeft();
			}
		}
		//发射子弹
		if(arg0.getKeyCode()==KeyEvent.VK_J) {
			if(this.hero.bb.size()<=4) {
				this.hero.shot();
			}
		}
		//必须重新绘制
		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.isHitEnemy();
			this.isHitHero();
			this.isCrashEnemy();
			this.isCrashHero();
			
			this.repaint();
		}
		
	}
	
}