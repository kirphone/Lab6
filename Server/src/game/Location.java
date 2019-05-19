package game;

public enum Location {
    FORREST {
        @Override
        public String toString() {
            return "FORREST";
        }
    },
    PYATACHOKHOME{
        @Override
        public String toString() {
            return "PYATACHOKHOME";
        }
    },
    RABBITHOME{
        @Override
        public String toString() {
            return "RABBITHOME";
        }
    },
    PUCHHOME{
        @Override
        public String toString() {
            return "PUCHHOME";
        }
    },
    ROBINHOME{
        @Override
        public String toString() {
            return "ROBINHOME";
        }
    }
}
