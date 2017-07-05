mkdir out

xcopy /E /Y natives out
xcopy /E /Y resources out

javac -sourcepath src/ -d out/ -cp libs/lwjgl.jar;libs/lwjgl-glfw.jar;libs/lwjgl-opengl.jar;libs/joml-1.9.3.jar src/de/diavololoop/game/Game.java
