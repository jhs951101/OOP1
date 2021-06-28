/**
 * Created on Sep 3, 2012
 * @author cskim -- hufs.ac.kr, Dept of CSE
 * Copy Right -- Free for Educational Purpose
 */
package report;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author cskim
 *
 */
public class MosaicImage extends JFrame {

	private JPanel contentPane;

	private static final int PROWS = 16;
	private static final int PSIZE = PROWS*PROWS;

	private JButton[] tiles = new JButton[PSIZE];
	private JButton[] mosaictiles = new JButton[PSIZE];
	private BufferedImage[] imageData = new BufferedImage[PSIZE];
	private BufferedImage tileImage = null;

	int scrHeight = 0;
	int scrWidth = 0;
    int bwidth = 0;
    int bheight = 0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MosaicImage frame = new MosaicImage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MosaicImage(){
		setBounds(100, 100, 550, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(PROWS, PROWS, 0, 0));
		initialize();
	}

	void initialize(){
		scrHeight = this.getHeight();
		scrWidth = this.getWidth();
		//System.out.println("w="+scrWidth+" h="+scrHeight);
		try {
			tileImage = ImageIO
					.read(getClass().getResource("/images/profile_small.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int pwidth = tileImage.getWidth()/PROWS;
		int pheight =  tileImage.getHeight()/PROWS;
        bwidth = scrWidth/PROWS;
        bheight = scrHeight/PROWS;

        for (int i=0; i<PROWS; ++i){
        	for (int j=0; j<PROWS; ++j){
        		imageData[PROWS*i+j] = 
        				tileImage.getSubimage(j*pwidth, i*pheight, pwidth, pheight);
        	}
        }
       
		for (int i=0; i<PSIZE; ++i){  // 일반 버튼들을 Panel에 추가함
			tiles[i] = new JButton();
			tiles[i].setIcon(new ImageIcon(imageData[i]));
			contentPane.add(tiles[i]);
		}
		
		for (int i=0; i<PSIZE; ++i){  // 모자이크 버튼들을 만들어놓음
			Color bcol = getAverageColor(imageData[i]);
			mosaictiles[i] = new JButton();
			mosaictiles[i].setIcon(new ImageIcon(OneColorBufferedImage.getBufferedImage(bwidth, bheight, bcol)));
		}
		
		for(int i=0; i<PSIZE; i++){
        	tiles[i].addActionListener(new ButtonListener1(i));
        	mosaictiles[i].addActionListener(new ButtonListener2(i));  // 배열 버튼에 액션을 추가함
        }
	}
	
	Color getAverageColor(BufferedImage tile){
		int twidth = tile.getWidth();
		int theight =  tile.getHeight();
		double pixSize = twidth*theight;
		double sumRed = 0;
		double sumGreen = 0;
		double sumBlue = 0;
		Color pixColor = null;
		for (int i=0; i<theight; ++i){
			for (int j=0; j<twidth; ++j){
				pixColor = new Color(tile.getRGB(i,j));
				sumRed += pixColor.getRed();
				sumGreen += pixColor.getGreen();
				sumBlue += pixColor.getBlue();
			}
		}
		int avgRed = (int)(sumRed/pixSize);
		int avgGreen = (int)(sumGreen/pixSize);
		int avgBlue = (int)(sumBlue/pixSize);
		return new Color(avgRed, avgGreen, avgBlue);
	}

	public class ButtonListener1 implements ActionListener {
		
		private int id = 0;  // id: 해당 버튼을 누르면 해당 버튼에 모자이크 화면이 뜨도록 해주는 식별자
		
		ButtonListener1(int i) {
			this.id = i;
		}
		// General Button Action (일반 버튼 액션)
		public void actionPerformed(ActionEvent arg0) {
			
			tiles[id].setVisible(false);
			contentPane.remove(tiles[id]);
			contentPane.add(mosaictiles[id], id);
			mosaictiles[id].setVisible(true);  // 버튼이 클릭되면 일반 버튼을 제거하고 모자이크화 된 버튼을 띄움
			
		}

	}
	
	public class ButtonListener2 implements ActionListener {
		
		private int id = 0;
		
		ButtonListener2(int i) {
			this.id = i;
		}
		// Mosaic Button Action (일반 버튼 액션)
		public void actionPerformed(ActionEvent arg0) {
			
			mosaictiles[id].setVisible(false);
			contentPane.remove(mosaictiles[id]);
			contentPane.add(tiles[id], id);
			tiles[id].setVisible(true);  // 버튼이 클릭되면 모자이크 버튼을 제거하고 다시 일반 버튼을 띄움
			
		}

	}
}
