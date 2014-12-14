// Snow.java
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Snow extends Frame {

  //Disp�N���X�̃C���X�^���X��
  Disp disp_0 = new Disp();

 public static void main(String args[]) {
  //Snow�N���X�̃C���X�^���X��
  new Snow();
 }

//�R���X�g���N�^
 public Snow() {
   super("Snow");
// ��ʏ�̃E�C���h�E�\���ʒu
   setLocation(100,100);
// �E�B���h�E�̃T�C�Y���w��
   setSize(640,400);

//�L�����o�X�̃T�C�Y
   disp_0.setSize(640,400);
//�L�����o�X���t���[���ɒǉ�
   add(disp_0);

// �E�B���h�E��������ɏI������悤�ɐݒ�
  addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent e) {System.exit(0);}
  });
// �E�B���h�E��\��
  setVisible(true);
 }
}

class Disp extends Canvas implements Runnable {

  //�ϐ��錾
  Image back_img;
  Image offscr_img;
  Graphics offscr_g;
  Drop yuki[];
  MediaTracker mt;
  Thread th;
  Toolkit tk = getToolkit();

//�R���X�g���N�^
  public Disp() {

    //�����󋵊Ď��p�I�u�W�F�N�g
    mt = new MediaTracker(this);
    //��ԃI�u�W�F�N�g�̐錾�i�z���200�ԕ��I�j
    yuki = new Drop[200];
    //�C���[�W�I�u�W�F�N�g��image�t�H���_�̉���kinokonomori.jpg�t�@�C�����Z�b�g
    back_img = tk.getImage(getClass().getResource("kinokonomori.jpg"));

    mt.addImage(back_img,0);

    //Drop�N���X�^��yuki[]�Ƃ����I�u�W�F�N�g�Ƃ��ď�Ő錾�����z�񕪗̈�m��
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
    //�����̊Ď�
    if (mt.checkID(0)){

      //�摜�T�C�Y�i��:640����:400�j�ɍ��킹�ăC���[�W�̗̈������
      offscr_img = createImage(640,400);
      offscr_g = offscr_img.getGraphics();

      //���F�̐ݒ�i����3���FR,G,B�̐��l�Ŏw��j
      offscr_g.setColor(new Color(255,255,255));
      //�����p�̃C���[�W�̈�ɂ��̂��̔w�i���Z�b�g
      offscr_g.drawImage(back_img, 0, 0, this);
      //��Ԃ̖��������[�v
      for (int i = 0; i < 200; i++){
        //��Ԃ̎c�������[�v
        for (int j = 0; j < 2; j++){
          //bl_start�t���O��false�Ȃ�Drop�N���X��start()���\�b�h�����s�Atrue�Ȃ�fall()���\�b�h�����s
          if (yuki[i].bl_start == false){
            yuki[i].start();
          }
          else{
            //��Ԃ̕`��(����10,�c��10�̔��F�œh��Ԃ��ꂽ�~�`��`��)
            offscr_g.fillOval(yuki[i].x,yuki[i].y,10,10);
            //��Ԃ̗���(���W�ʒu�̈ړ�)
            yuki[i].fall();
          }
        }
      }
      //�����p�̃C���[�W�̈�ɃZ�b�g���ꂽ���̂��{��Ԃ̍����C���[�W���u���E�U�ɕ\��
      g.drawImage(offscr_img,0,0,this);
    }
  }
}

//�����N���X
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
    //X���W�̊J�n�ʒu�������_���ɐݒ�
    x = (int) (Math.random() * w);
    //Y���W�̊J�n�ʒu�������_���ɐݒ�(������3/5�͈̔͂Ŏw��)
    y = (int) (Math.random() * h);
  }
  void fall(){
    int r;
    //��ԗ������̍��E�̂������0����10�܂ł̃����_���̐��l�ŏ����𕪊�
    //�i5�`10�܂ł̏ꍇ�͍��W�ړ��Ȃ��ŗ����j
    r = (int)(Math.random() * 10);
    switch (r){
      case 1:
        x--;       //���ւP�R�}�ړ�
        break;
      case 2:
        x++;       //�E�ւP�R�}�ړ�
        break;
      case 3:
        x = x-2;   //���ւQ�R�}�ړ�
        break;
      case 4:
        x = x+2;   //�E�ւQ�R�}�ړ�
    }
    y ++;          //���ւP�R�}�ړ�
    //��Ԃ̍��W����ʂ���t�F�[�h�A�E�g������t���O��false�ɂ���
    if ((x < -20) || (x >= w+20) || (y >= h))
      bl_start = false;
  }
}
