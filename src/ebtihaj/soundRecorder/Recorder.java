package ebtihaj.soundRecorder;

public class Recorder {
	
	static SimpleAudioRecorder recorder = SimpleAudioRecorder.getInstance();
	public void recorder() {
			if(recorder.isAlive()){
				System.out.println("Stopped recording");
					recorder.stopRecording();
					}
				else{
					System.out.println("Recording started");
					recorder.start();
			}}
}