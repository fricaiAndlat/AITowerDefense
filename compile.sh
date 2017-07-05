mkdir out/

cp -r natives/* out/
cp -r resources/* out/

javac -sourcepath src/ -d out/ -cp libs:libs/lwjgl.jar:libs/lwjgl-glfw.jar:libs/lwjgl-opengl.jar:libs/joml-1.9.3.jar src/de/diavololoop/game/Game.java
