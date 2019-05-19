package game;

public enum Color {
    START{
        @Override
        public String toString() {
            return "Базовый";
        }
    },
    SWEET{
        @Override
        public String toString() {
            return "Милый";
        }
    },
    COZY{
        @Override
        public String toString() {
            return "Уютный";
        }
    },
    FAMILIAR{
        @Override
        public String toString() {
            return "Привычный";
        }
    }
}
