package TankGame4;

import java.util.Vector;

class Tank{
	int x=0;
	int y=0;
	// 0表示上，1表示右，2表示下，3表示左
	int direction=0;
	//设置坦克的速度
	int speed=2;
	int color;
	boolean isLive=true;
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Tank(int x, int y) {
		this.x=x;
		this.y=y;
	}
}

class Hero extends Tank{
	Vector<Bullet> bb=new Vector<Bullet>();
	Bullet b=null;
	int speed=4;
	public Hero(int x, int y) {
		super(x, y);
	}
	public void moveUp() {
		y-=speed;
	}
	public void moveRight() {
		x+=speed;
	}
	public void moveDown() {
		y+=speed;
	}
	public void moveLeft() {
		x-=speed;
	}
	public void shot() {
		
		switch(this.direction) {
		case 0:
			b=new Bullet(x+5, y-10, 0);
			bb.add(b);
			break;
		case 1:
			b=new Bullet(x+20, y+5, 1);
			bb.add(b);
			break;
		case 2:
			b=new Bullet(x+5, y+20, 2);
			bb.add(b);
			break;
		case 3:
			b=new Bullet(x-10, y+5, 3);
			bb.add(b);
			break;
		}
		Thread t=new Thread(b);
		t.start(); 
	}
}

class Enemy extends Tank implements Runnable{
	int times=0;
	//定义一个向量让敌人也能发子弹
	Vector<Bullet> bb=new Vector<Bullet>(); 
	public Enemy(int x, int y) {
		super(x, y);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			boolean state=true;
		
			switch(this.direction) {
			case 0:
				//说明坦克向上移动
				for(int i=0;i<30&&state;i++) {
					if(this.direction!=0) {
						state=false;
						break;
					}
					if(y>0) {
						y-=speed;
					}
					try {
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for(int i=0;i<30&&state;i++) {
					if(this.direction!=1) {
						state=false;
						break;
					}
					if(x<800) {
						x+=speed;
					}
					
				try {
					Thread.sleep(50);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
				
				break;
			case 2:
				for(int i=0;i<30&&state;i++) {
					if(this.direction!=2) {
						state=false;
						break;
					}
					if(y<600) {
						y+=speed;
					}
					
					try {
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
				break;
			case 3:
				for(int i=0;i<30&&state;i++) {
					if(this.direction!=3) {
						state=false;
						break;
					}
					if(x>0) {
						x-=speed;
					}
					
					try {
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
				break;
			}
			this.times++;
			
			//判断是否需要给坦克加子弹
			if(times%2==0) {
				if(isLive) {
					if(bb.size()<5) {
						Bullet b=null;
						switch(direction) {
						case 0:
							b=new Bullet(x+5, y-10, 0);
							bb.add(b);
							break;
						case 1:
							b=new Bullet(x+20, y+5, 1);
							bb.add(b);
							break;
						case 2:
							b=new Bullet(x+5, y+20, 2);
							bb.add(b);
							break;
						case 3:
							b=new Bullet(x-10, y+5, 3);
							bb.add(b);
							break;
						}
						Thread bullet=new Thread(b);
						bullet.start();
					}
				}
			}
			
//			for(int i=0;i<enemis.size();i++) {
//				Enemy enemy=enemis.get(i);
//				if(enemy.isLive) {
//					if(enemy.bb.size()<1) {
//						Bullet b=null;
//						switch(enemy.direction) {
//						case 0:
//							b=new Bullet(enemy.x+5, enemy.y-10, 0);
//							enemy.bb.add(b);
//							break;
//						case 1:
//							b=new Bullet(enemy.x+20, enemy.y+5, 1);
//							enemy.bb.add(b);
//							break;
//						case 2:
//							b=new Bullet(enemy.x+5, enemy.y+20, 2);
//							enemy.bb.add(b);
//							break;
//						case 3:
//							b=new Bullet(enemy.x-10, enemy.y+5, 3);
//							enemy.bb.add(b);
//							break;
//						}
//						Thread bullet=new Thread(b);
//						bullet.start();
//					}
//				}
//			}
			
			//让坦克随机产生一个新的方向
			this.direction=(int)(Math.random()*4);
			
			//判断敌人坦克是否死亡
			if(this.isLive==false) {
				//让坦克死亡后退出线程
				break;
			}
//			//判断子弹是否没有了
//			if(bb.size()<1) {
//				Bullet b_enemy=new Bullet();
//			}
			
		}
	}
}

class Bullet implements Runnable{
	 int x;
	 int y;
	 int direction;
	 int speed=3;
	 boolean isLive=true;
	 public Bullet(int x, int y, int direction) {
		 this.x=x;
		 this.y=y;
		 this.direction=direction;
	 }
	 public void run() {
		 while(true) {
			 try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 switch(this.direction) {
			 case 0:
				 y-=speed;
				 break;
			 case 1:
				 x+=speed;
				 break;
			 case 2:
				 y+=speed;
				 break;
			 case 3:
				 x-=speed;
				 break;
			 }
			 //子弹何时消失？
			 if(x<0||x>800||y<0||y>600) {
				 this.isLive=false;
				 break;
			 }
		 }
	 }
}

class Bomb{
	int x,y;
	//炸弹的生命
	int life=9;
	boolean isLive=true;
	public Bomb(int x, int y) {
		this.x=x;
		this.y=y;
	}
	//减少生命值
	public void lifeDown() {
		if(life>0) {
			life--;
		}else {
			this.isLive=false;
		}
	}
}