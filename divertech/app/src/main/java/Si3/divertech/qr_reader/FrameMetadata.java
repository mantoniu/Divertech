package Si3.divertech.qr_reader;

public class FrameMetadata {
    private final int height;
    private final int rotation;
    private final int width;

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRotation() {
        return this.rotation;
    }

    private FrameMetadata(int width, int height, int rotation) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    /* loaded from: classes4.dex */
    public static class Builder {
        private int height;
        private int rotation;
        private int width;

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setRotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public FrameMetadata build() {
            return new FrameMetadata(this.width, this.height, this.rotation);
        }
    }
}
