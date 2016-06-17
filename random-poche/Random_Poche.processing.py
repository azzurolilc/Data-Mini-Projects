//Random Poche

int num = 3;
float[]x = new float[num];
float[]y = new float[num];
float[]r = new float[num];
float[]xSpeed = new float[num];
float[]ySpeed = new float[num];
//color[]colors = new color[num];


void setup(){
  background(250);
  size(1000,700);
  frameRate(18);
  noStroke();
  int m;
  for (int i=0; i<num; i++){
       m = (int)(random(4));
    if (m==1){
    x[i]=width;
    y[i]=random(0,height);
    }
    else if (m==2){
      x[i]=0;
      y[i]=random(0,height);

    }
    else if (m==3){
      x[i]=random(0,width);
      y[i]=height;

    }
    else{
       x[i]=random(0,width);
       y[i]=0;
    }
    r[i]=random(10, 15);
    xSpeed[i]=random(1.01,2)+noise(3)+.01;
    ySpeed[i]=noise(random(2,6))*random(7,9)+.01;
  }
}

void move(int n){
  x[n]+=xSpeed[n];
  y[n]+=ySpeed[n];
  int k=n;
  if (x[k] >= width||x[k]<=0) {
      xSpeed[k]=xSpeed[k]*random(random(-1.2,-1),-1)*noise(7)*2;
      if (xSpeed[k]>12||xSpeed[k]<-12){
        xSpeed[k]=xSpeed[k]/random(6,10);
      }
     } 
     
  if (y[k] >= height|| y[k]<=0) {
      ySpeed[k]= ySpeed[k]*random(-1.2,-1.1)*noise(7,3)*3;
      if (ySpeed[k]>12||ySpeed[k]<-12){
        ySpeed[k]=ySpeed[k]/random(4,10);
      }
     }
  
  
}

void lines(){
  stroke(.01);
  strokeWeight(.01);  
        line(x[2],y[2],x[0],y[0]);
        line(x[1],y[1],x[0],y[0]);
        line(x[1],y[1],x[2],y[2]);
}


void draw(){
  for (int k=0; k<num; k++){
    //colors[k]=color(random(170,255), random(120,255), random(150,210), random(5,70));
    //fill(colors[k]);
    noStroke();
    //ellipse(x[k],y[k],r[k]*2,r[k]*2);
    move(k);
  }
  lines();
}
