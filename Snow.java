// Snow.java
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Snow extends Frame {

  //Dispクラスのインスタンス化
  Disp disp_0 = new Disp();

 public static void main(String args[]) {
  //Snowクラスのインスタンス化
  new Snow();
 }

//コンストラクタ
 public Snow() {
   super("Snow");
// 画面上のウインドウ表示位置
   setLocation(100,100);
// ウィンドウのサイズを指定
   setSize(640,400);

//キャンバスのサイズ
   disp_0.setSize(640,400);
//キャンバスをフレームに追加
   add(disp_0);

// ウィンドウを閉じた時に終了するように設定
  addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent e) {System.exit(0);}
  });
// ウィンドウを表示
  setVisible(true);
 }
}

class Disp extends Canvas implements Runnable {

  //変数宣言
  Image back_img;
  Image offscr_img;
  Graphics offscr_g;
  Drop yuki[];
  MediaTracker mt;
  Thread th;
  Toolkit tk = getToolkit();

//コンストラクタ
  public Disp() {

    //処理状況監視用オブジェクト
    mt = new MediaTracker(this);
    //雪つぶオブジェクトの宣言（配列で200つぶ分！）
    yuki = new Drop[200];
    //イメージオブジェクトにimageフォルダの下のkinokonomori.jpgファイルをセット
    back_img = tk.getImage(getClass().getResource("kinokonomori.jpg"));

    mt.addImage(back_img,0);

    //Dropクラス型をyuki[]というオブジェクトとして上で宣言した配列分領域確保
    for (int i = 0; i < 200; i++){
      yuki[i]=new Drop(640,400);
    }

    th = new Thread(this);
    th.start();
  }

 public void run(){
  Thread me = Thread.currentThread();
  try{
   mt.waitForID(0);
  }catch(InterruptedException e){
    }
     while (th == me) {
   repaint();
   try {
    me.sleep(100);
   } catch (InterruptedException e){
      }
  }
 }
 public void update(Graphics g){
  paint(g);
 }
  public void paint(Graphics g){
    //処理の監視
    if (mt.checkID(0)){

      //画像サイズ（幅:640高さ:400）に合わせてイメージの領域を準備
      offscr_img = createImage(640,400);
      offscr_g = offscr_img.getGraphics();

      //白色の設定（光の3原色R,G,Bの数値で指定）
      offscr_g.setColor(new Color(255,255,255));
      //準備用のイメージ領域にきのこの背景をセット
      offscr_g.drawImage(back_img, 0, 0, this);
      //雪つぶの枚数分ループ
      for (int i = 0; i < 200; i++){
        //雪つぶの残像分ループ
        for (int j = 0; j < 2; j++){
          //bl_startフラグがfalseならDropクラスのstart()メソッドを実行、trueならfall()メソッドを実行
          if (yuki[i].bl_start == false){
            yuki[i].start();
          }
          else{
            //雪つぶの描画(横幅10,縦幅10の白色で塗りつぶされた円形を描く)
            offscr_g.fillOval(yuki[i].x,yuki[i].y,10,10);
            //雪つぶの落下(座標位置の移動)
            yuki[i].fall();
          }
        }
      }
      //準備用のイメージ領域にセットされたきのこ＋雪つぶの合成イメージをブラウザに表示
      g.drawImage(offscr_img,0,0,this);
    }
  }
}

//落下クラス
class Drop {
  boolean bl_start;
  int x, y, w, h;
  Drop(int bw,int bh){
    x = 0; y = 0;
    w = bw;
    h = bh;
    bl_start = false;
  }
  void start(){
    bl_start = true;
    //X座標の開始位置をランダムに設定
    x = (int) (Math.random() * w);
    //Y座標の開始位置をランダムに設定(下から3/5の範囲で指定)
    y = (int) (Math.random() * h);
  }
  void fall(){
    int r;
    //雪つぶ落下時の左右のちらつきを0から10までのランダムの数値で処理を分岐
    //（5～10までの場合は座標移動なしで落下）
    r = (int)(Math.random() * 10);
    switch (r){
      case 1:
        x--;       //左へ１コマ移動
        break;
      case 2:
        x++;       //右へ１コマ移動
        break;
      case 3:
        x = x-2;   //左へ２コマ移動
        break;
      case 4:
        x = x+2;   //右へ２コマ移動
    }
    y ++;          //下へ１コマ移動
    //雪つぶの座標が画面からフェードアウトしたらフラグをfalseにする
    if ((x < -20) || (x >= w+20) || (y >= h))
      bl_start = false;
  }
}
