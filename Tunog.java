import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Tunog{
	private Clip clip;
	private Sequencer sequencer;
	Tunog(String fname){
		URL source;
		try{
			source = new java.io.File(fname).toURI().toURL();
			if(fname.toLowerCase().endsWith(".mid")){
				sequencer=MidiSystem.getSequencer();
				sequencer.open();
				try(InputStream midi=source.openStream()){
					sequencer.setSequence(midi);
				}
			}
			else{
				try(AudioInputStream audio=AudioSystem.getAudioInputStream(source)){
					clip=AudioSystem.getClip();
					clip.open(audio);
				}
			}
		}
		catch(IOException | InvalidMidiDataException |
				MidiUnavailableException | UnsupportedAudioFileException |
				LineUnavailableException exception){
			System.err.println("Unable to load audio file '" + fname + "': " + exception.getMessage());
		}
		}
	public void play(){
		stop();
		if(clip!=null)
			clip.start();
		else if(sequencer!=null)
			sequencer.start();
		}
	public void stop(){
		if(clip!=null)
			clip.stop();
		if(sequencer!=null)
			sequencer.stop();
		}
	public void loop(){
		stop();
		if(clip!=null){
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if(sequencer!=null){
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
		}
		}
	}