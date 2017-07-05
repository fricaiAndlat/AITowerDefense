package de.diavololoop.gui;

/**
 * Created by Chloroplast on 01.06.2017.
 */
public class Animation {

    private final GuiImage[] images;
    private final boolean loop;

    private int pos;
    private final long duration;

    public Animation(int size, boolean loop, double duration){
        images = new GuiImage[size];
        this.loop = loop;
        this.duration = (long) (duration*1000);
    }

    public void addTick(GuiImage image){
        images[pos++] = image;
        pos %= images.length;
    }

    public AnimationIterator getIterator(){
        return new AnimationIterator(this);
    }

    public long getDuration() {
        return duration;
    }

    public boolean isLoop(){
        return loop;
    }

    public class AnimationIterator {

        private final Animation animation;
        private long startTime = -1;

        private AnimationIterator(Animation animation) {

            this.animation = animation;

        }

        public GuiImage next(){

            if(startTime == -1){
                startTime = System.currentTimeMillis();
            }

            if(!animation.loop && System.currentTimeMillis() - startTime > duration){
                return GuiImage.EMPTY;
            }

            int frame = (int) (((System.currentTimeMillis() - startTime) % duration) * animation.images.length / duration);

            return animation.images[frame];

        }

        public Animation getOrigin(){
            return animation;
        }

    }


}
