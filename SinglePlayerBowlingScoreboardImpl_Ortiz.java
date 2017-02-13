package bowling;
/*Amanda Ortiz
 * COSC 3327 - Algorithms - Dr. Kart 
 * Challenge VII - Bowling with Full Scoreboard
 * Spring 2016
 */
import test.Points;

public class SinglePlayerBowlingScoreboardImpl_Ortiz implements SinglePlayerBowlingScoreboard, AssignmentMetaData
{
	private static final int MAXIMUM_ROLLS = 21;	//Maximum rolls in a one player game
	private String playerName;
	private int[] pinsKnockedDownArray = new int[MAXIMUM_ROLLS];
	private int rollCount = 0;

	public SinglePlayerBowlingScoreboardImpl_Ortiz(String playerName)
	{
		assert playerName != null : "playerName is null!";
		this.playerName = playerName;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	//part of pre: !isGameOver()
	//part of post: 1 <= rv <= 10
	public int getCurrentFrame() 
	{
		assert !isGameOver() : "Game is over!";

		int currentFrame = 1;
		int index = 0;
		int numberOfRolls = 0;
		while (numberOfRolls < rollCount && currentFrame < 10)
		{
			if(pinsKnockedDownArray[index] == 10)
			{
				index++;
				currentFrame++;
				numberOfRolls++;
			}
			else if ((numberOfRolls+2) <= rollCount && currentFrame < 10)
			{
				index = index+2;
				numberOfRolls = numberOfRolls + 2;
				currentFrame++;
			}
			else if((numberOfRolls+1) <= rollCount && currentFrame < 10)
			{
				index = index + 2;
				numberOfRolls = numberOfRolls + 2;
			}
		}

		return currentFrame;

	}

	/*part of pre: 1 <= frameNumber <= 10
	//part of pre: 1 <= boxIndex <= 3
//part of pre: frameNumber <= getCurrentFrame()
//part of pre: (frameNumber != getCurrentFrame()) || (getCurrentBall() > boxIndex)
	//part of pre: frameNumber = 10 ==> isGameOver()
	  part of pre: (frameNumber == 10) ==> ( 1 <= boxIndex <= 3)
	//part of pre: (1 <= frameNumber < 10) ==> (1 <= boxIndex <= 2)
	//part of pre: boxIndex = 3 ==> frameNumber = 10
	//part of post: ((rv = Mark.STRIKE) && (1 <= frameNumber <= 9)) ==> (boxIndex == 2)
	//part of post: ((rv = Mark.SPARE) && (1 <= frameNumber <= 9)) ==> (boxIndex == 2)
	//part of post: ((rv = Mark.SPARE) && (frameNumber = 10)) ==> (boxIndex >= 2)

	 */
	public Mark getMark(int frameNumber, int boxIndex) 
	{	
		//------------ASSERTIONS-------------------------------------------
		assert 1 <= frameNumber : "frameNumber = " + frameNumber + " < 1!";
		assert frameNumber <= 10 : "frameNumber = " + frameNumber + " > 10!";
		assert 1 <= boxIndex : "boxIndex = " + boxIndex + " < 1!";
		assert boxIndex <= 3 : "boxIndex = " + boxIndex + " > 3!";

		if(!isGameOver())
		{
			assert frameNumber <= getCurrentFrame();
			assert (frameNumber != getCurrentFrame()) || (getCurrentBall() > boxIndex);
		}
		if(frameNumber == 10 && !isGameOver())
		{
			assert getCurrentBall() > boxIndex;
		}
		if (frameNumber == 10 )
		{
			assert (boxIndex == 1 || boxIndex == 2 || boxIndex == 3);
		}
		if(1 <= frameNumber && frameNumber < 10)
		{
			assert (1 <= boxIndex && boxIndex <=2);

		}
		if(boxIndex == 3 )
		{
			assert frameNumber == 10;
		}
		//-----------------------------------------------------------------
		//		Exercise for student: Fix
		//		assert (boxIndex != 2) || (!isStrike(frameNumber) && !isSpare(frameNumber)) : "boxIndex = " + boxIndex + ", but there was a Strike! : frameNumber = " + frameNumber;
		//		assert (boxIndex != 2) || (!isSpare(frameNumber)) : "boxIndex = " + boxIndex + ", but there was a Spare! : frameNumber = " + frameNumber;
		//-----------------------------------------------------------------
		Mark mark = null;
		if(frameNumber < 10)
		{
			mark = getMarkSingleDigitFrameNumber(frameNumber, boxIndex);
		}
		else mark = getMarkTenthFrame(boxIndex);
		assert mark != null : "mark is null!";


		return mark;

	}

	private Mark getMarkSingleDigitFrameNumber(int frameNumber, int boxIndex)
	{
		assert 1 <= frameNumber : "frameNumber = " + frameNumber + " < 1!";
		assert frameNumber <= 9 : "frameNumber = " + frameNumber + " > 9!";
		assert 1 <= boxIndex : "boxIndex = " + boxIndex + " < 1!";
		assert boxIndex <= 2 : "boxIndex = " + boxIndex + " > 2!";

		int indexOfScanningThroughFrames = 1;
		int arrayIndex = 0;

		while (indexOfScanningThroughFrames < frameNumber) // looping through frames to check if score == 10, if == 10, then advances to next frame
		{
			if(pinsKnockedDownArray[arrayIndex] == 10)
			{
				indexOfScanningThroughFrames++;
				arrayIndex++;
			}
			else 
			{
				arrayIndex = arrayIndex + 2;
				indexOfScanningThroughFrames++;
			}
		}

		if(pinsKnockedDownArray[arrayIndex] == 10) //if pins knocked down in array == 10, send EMPTY if (1,1), 
			// send STRIKE if (1,2)
		{
			if(boxIndex == 1)
			{
				return Mark.EMPTY;
			}
			else
			{
				return Mark.STRIKE;
			}
		}


		if(boxIndex == 2)
		{
			if(pinsKnockedDownArray[arrayIndex + boxIndex - 1] + pinsKnockedDownArray[arrayIndex + boxIndex -2] == 10)
			{
				return Mark.SPARE;
			}
		}
		return Mark.translate(pinsKnockedDownArray[arrayIndex+boxIndex-1]); //otherwise, return the correct translated mark

	}

	private Mark getMarkTenthFrame(int boxIndex)
	{
		assert 1 <= boxIndex : "boxIndex = " + boxIndex + " < 1!";
		assert boxIndex <= 3 : "boxIndex = " + boxIndex + " > 3!";

		int arrayIndex = 0;
		int frameNumber = 1;
		Mark mark = null;
		while(frameNumber < 10)
		{
			if(pinsKnockedDownArray[arrayIndex] == 10)
			{
				frameNumber++;
				arrayIndex++;
			}
			else
			{
				arrayIndex = arrayIndex + 2;
				frameNumber++;
			}
		}// end while
		boolean arePreviousAndCurrentPinsTotalEqualToTen = (pinsKnockedDownArray[arrayIndex+boxIndex-2] + pinsKnockedDownArray[arrayIndex + boxIndex -1] == 10);
		if(boxIndex == 2 && pinsKnockedDownArray[arrayIndex+boxIndex-1]!=10 && arePreviousAndCurrentPinsTotalEqualToTen)
		{
			mark = Mark.SPARE;
		}
		else if(boxIndex == 2 && pinsKnockedDownArray[arrayIndex+boxIndex-2] == 10 && pinsKnockedDownArray[arrayIndex+boxIndex-1]==10 )
		{
			mark = Mark.STRIKE;
		}
		else if(boxIndex == 3 && arePreviousAndCurrentPinsTotalEqualToTen && pinsKnockedDownArray[arrayIndex+boxIndex - 2] != 10)
		{
			mark = Mark.STRIKE;
		}

		if(pinsKnockedDownArray[arrayIndex + boxIndex - 1] != 10)
		{
			return Mark.translate(pinsKnockedDownArray[arrayIndex + boxIndex - 1]);
		}
		else
		{
			mark = Mark.STRIKE;
		}
		return mark;
	}

	public int getScore(int frameNumber) {
		//----------------------------------assertions: ------------------------------
		assert 1 <= frameNumber : "frameNumber = " + frameNumber + " < 1!";
		assert frameNumber <= 10 : "frameNumber = " + frameNumber + " > 10!";

		if( !isGameOver())
		{
			assert getCurrentFrame() > frameNumber;
			if(getCurrentFrame() == (frameNumber+2))
			{
				assert !(Mark.STRIKE.equals(getMark(frameNumber,2)) && Mark.STRIKE.equals(getMark(frameNumber + 1, 2)));
			}
			if(getCurrentFrame() == frameNumber + 1)
			{
				assert !(Mark.STRIKE.equals(getMark(frameNumber, 2)));
			}
			if(getCurrentFrame() == frameNumber + 1 && getCurrentBall() ==1)
			{
				assert !(Mark.SPARE.equals(getMark(frameNumber, 2)));
			}
		}
		if(frameNumber == 10)
		{
			assert isGameOver(): "Game is not over !";
		}


		//-----------------------------------------------------------------------
		int score = 0;
		int currentFrame = 0;
		for (int i=0; i<rollCount; i++) {
			if (currentFrame<frameNumber && currentFrame < 10) 
			{
				if (pinsKnockedDownArray[i]==10) {
					score += (pinsKnockedDownArray[i] + pinsKnockedDownArray[i+1] + pinsKnockedDownArray[i+2]);
					currentFrame++;
				}
				else if (pinsKnockedDownArray[i] + pinsKnockedDownArray[i+1]==10) {
					score += (pinsKnockedDownArray[i] + pinsKnockedDownArray[i+1] + pinsKnockedDownArray[i+2]);
					i++;
					currentFrame++;
				}
				else {
					score += pinsKnockedDownArray[i];
					score += pinsKnockedDownArray[i+1];
					i++;
					currentFrame++;
				}
				
			}	
			else if(currentFrame == 11)
			{
				score += (pinsKnockedDownArray[i] + pinsKnockedDownArray[i+1] + pinsKnockedDownArray[i+2]);
			}



		}

		return score;

	}
	public boolean isGameOver() 
	{
		int currentFrameScan = 1;
		int arrayIndex = 0;
		int numberOfStrikesRolled = 0;
		int ballsRolledWithoutStrike = 0;
		while(currentFrameScan < 10)
		{
			if(pinsKnockedDownArray[arrayIndex] == 10)
			{
				currentFrameScan++;
				numberOfStrikesRolled++;
				arrayIndex++;
			}
			else
			{
				arrayIndex = arrayIndex+2;
				currentFrameScan++;
				ballsRolledWithoutStrike = ballsRolledWithoutStrike + 2;
			}
		}
		if(arrayIndex >= rollCount)
		{
			return false;
		}

    	int numberOfRollsThrown = numberOfStrikesRolled + ballsRolledWithoutStrike;

		if(pinsKnockedDownArray[arrayIndex] == 10 && numberOfRollsThrown + 3 == rollCount)
		{
			return true;
		}
		else if(pinsKnockedDownArray[arrayIndex] + pinsKnockedDownArray[arrayIndex+1] == 10 && numberOfRollsThrown+3 == rollCount && pinsKnockedDownArray[arrayIndex] != 10)
		{
			return true;
		}
		else if (pinsKnockedDownArray[arrayIndex] + pinsKnockedDownArray[arrayIndex+1] != 10 && numberOfRollsThrown+2 == rollCount && pinsKnockedDownArray[arrayIndex] != 10)
		{
			return true;
		}
		
			//game is over if each frame has a strike, and 10th frame has 3 strikes. 
			return false;

	}
	//part of pre: 0 <= pinsKnockedDown <= 10
	//much more here...
	public void recordRoll(int pinsKnockedDown) 
	{
		assert 0 <= pinsKnockedDown : "pinsKnockedDown = " + pinsKnockedDown + " < 0!";
		assert pinsKnockedDown <= 10 : "pinsKnockedDown = " + pinsKnockedDown + " > 10!";
		assert (getCurrentBall() == 1) || 
		((getCurrentBall() == 2) && (((getCurrentFrame() == 10) && isStrikeOrSpare(getMark(10, 1))) || ((pinsKnockedDownArray[rollCount - 1] + pinsKnockedDown) <= 10))) || 
		((getCurrentBall() == 3) && (((getCurrentFrame() == 10) && isStrikeOrSpare(getMark(10, 2))) || ((pinsKnockedDownArray[rollCount - 1] + pinsKnockedDown) <= 10)));
		assert !isGameOver() : "Game is over!";

		pinsKnockedDownArray[rollCount] = pinsKnockedDown;
		rollCount++;
	}
	//part of pre: !isGameOver()
	//part of post: 1 <= rv <= 3
	//part of post: frameNumber < 10 ==> rv <= 2
	//part of post: ((frameNumber < 10) && (rv == 2)) ==> getMark(frameNumber, 1).equals(Mark.STRIKE)

	public int getCurrentBall() 
	{
		assert !isGameOver() : "Game is over!";

		int currentBall = 1;

		if(getCurrentFrame() != 10)
		{
			int strikeRolledCount  = 10;
			int numberOfStrikes = 0;
			for(int arrayIndex = 0; arrayIndex < rollCount; arrayIndex++)
			{
				if(pinsKnockedDownArray[arrayIndex] == strikeRolledCount)
				{
					numberOfStrikes++;
				}
			}//end for loop
			if((rollCount - numberOfStrikes)%2 == 0)
			{
				currentBall = 1;
			}
			else
			{
				currentBall = 2;
			}
		}
		else
		{
			int arrayIndex = 0;
			int frameNumber = 1;
			while(frameNumber < 10)
			{
				if(pinsKnockedDownArray[arrayIndex] == 10)
				{
					frameNumber++;
					arrayIndex++;
				}
				else
				{
					arrayIndex = arrayIndex + 2;
					frameNumber++;
				}
			}// end while
			while(arrayIndex < rollCount)
			{
				arrayIndex++;
				currentBall++;
			}
		}


		return currentBall;

	}

//	private static final String VERTICAL_SEPARATOR = "#";
//	private static final String HORIZONTAL_SEPARATOR = "#";
//	private static final String LEFT_EDGE_OF_SMALL_SQUARE = "[";
//	private static final String RIGHT_EDGE_OF_SMALL_SQUARE = "]";
	private String getScoreboardDisplay()
	{
		return "Scoreboard goes here";
//		StringBuffer frameNumberLineBuffer = new StringBuffer();
//		StringBuffer markLineBuffer = new StringBuffer();
//		StringBuffer horizontalRuleBuffer = new StringBuffer();
//		StringBuffer scoreLineBuffer = new StringBuffer();
//		frameNumberLineBuffer.append(VERTICAL_SEPARATOR);
//
//		markLineBuffer.append(VERTICAL_SEPARATOR);
//		horizontalRuleBuffer.append(VERTICAL_SEPARATOR);
//		scoreLineBuffer.append(VERTICAL_SEPARATOR);
//		int theCurrentFrame = 0;
//		if (isGameOver()) {
//			theCurrentFrame=10;
//		}
//		else {
//			theCurrentFrame=getCurrentFrame();
//		}
//
//		for(int frameNumber = 1; frameNumber <= 9; frameNumber++)
//		{
//			frameNumberLineBuffer.append("  " + frameNumber + "  ");
//			markLineBuffer.append(" ");
//
//			if(frameNumber < getCurrentFrame())
//			{
//				markLineBuffer.append(getMark(frameNumber, 1));
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(getMark(frameNumber, 2));
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//
//			}
//			else if(frameNumber == getCurrentFrame() && getCurrentBall() > 1 )
//			{
//				markLineBuffer.append(getMark(frameNumber,1));
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//			}
//			else
//			{
//				markLineBuffer.append(" ");
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//			}
//			final int CHARACTER_WIDTH_SCORE_AREA = 5;
//			for(int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) horizontalRuleBuffer.append(HORIZONTAL_SEPARATOR);
//			if(isGameOver() || frameNumber < getCurrentFrame())
//			{
//				int score = getScore(frameNumber);
//				final int PADDING_NEEDED_BEHIND_SCORE = 1;
//				final int PADDING_NEEDED_IN_FRONT_OF_SCORE = CHARACTER_WIDTH_SCORE_AREA - ("" + score).length() - PADDING_NEEDED_BEHIND_SCORE;
//				for(int i = 0; i < PADDING_NEEDED_IN_FRONT_OF_SCORE; i++) scoreLineBuffer.append(" ");
//				scoreLineBuffer.append(score);
//				for(int i = 0; i < PADDING_NEEDED_BEHIND_SCORE; i++) scoreLineBuffer.append(" ");
//			}
//			else
//			{
//				for(int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) scoreLineBuffer.append(" ");
//			}
//
//			frameNumberLineBuffer.append(VERTICAL_SEPARATOR);
//			markLineBuffer.append(VERTICAL_SEPARATOR);
//			horizontalRuleBuffer.append(VERTICAL_SEPARATOR);
//			scoreLineBuffer.append(VERTICAL_SEPARATOR);
//
//		}
//		//Frame 10:
//		{
//			final String THREE_SPACES = "   ";
//			frameNumberLineBuffer.append(THREE_SPACES + 10 + THREE_SPACES);
//
//			markLineBuffer.append(" ");
//			if(isGameOver() && getCurrentFrame() == 10)
//			{
//				markLineBuffer.append(getMark(10, 1));
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(getMark(10, 2));
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(getMark(10, 3));
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//			}
//			else if(getCurrentBall() == 2 &&theCurrentFrame == 10)
//			{
//				markLineBuffer.append((getMark(10,1)));
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//			}
//			else if(getCurrentBall() == 3 &&theCurrentFrame == 10)
//			{
//				markLineBuffer.append((getMark(10,1)));
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//			}
////			else if (getCurrentFrame() == 10 && getCurrentBall() == 2)
////			{
////				markLineBuffer.append(" ");
////				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
////				markLineBuffer.append(" ");
////				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
////				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
////				markLineBuffer.append(" ");
////				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
////			}
//
//			else
//			{
//				markLineBuffer.append(" ");
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(LEFT_EDGE_OF_SMALL_SQUARE);
//				markLineBuffer.append(" ");
//				markLineBuffer.append(RIGHT_EDGE_OF_SMALL_SQUARE);
//			}
//			final int CHARACTER_WIDTH_SCORE_AREA = 8;
//			for(int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) horizontalRuleBuffer.append(HORIZONTAL_SEPARATOR);
//			if(isGameOver())
//			{
//				int score = getScore(10);
//				final int PADDING_NEEDED_BEHIND_SCORE = 1;
//				final int PADDING_NEEDED_IN_FRONT_OF_SCORE = CHARACTER_WIDTH_SCORE_AREA - ("" + score).length() - PADDING_NEEDED_BEHIND_SCORE;
//				for(int i = 0; i < PADDING_NEEDED_IN_FRONT_OF_SCORE; i++) scoreLineBuffer.append(" ");
//				scoreLineBuffer.append(score);
//				for(int i = 0; i < PADDING_NEEDED_BEHIND_SCORE; i++) scoreLineBuffer.append(" ");
//			}
//			else
//			{
//				for(int i = 0; i < CHARACTER_WIDTH_SCORE_AREA; i++) scoreLineBuffer.append(" ");
//			}
//
//			frameNumberLineBuffer.append(VERTICAL_SEPARATOR);
//			markLineBuffer.append(VERTICAL_SEPARATOR);
//			horizontalRuleBuffer.append(VERTICAL_SEPARATOR);
//			scoreLineBuffer.append(VERTICAL_SEPARATOR);
//		}
//
//		return 	getPlayerName() + "\n" +
//		horizontalRuleBuffer.toString() + "\n" +
//		frameNumberLineBuffer.toString() + "\n" +
//		horizontalRuleBuffer.toString() + "\n" +
//		markLineBuffer.toString() + "\n" +
//		scoreLineBuffer.toString() + "\n" +
//		horizontalRuleBuffer.toString();
	}
	//rv = r1 + "\n" + r2, where r1 = " " and r2 = " "
	//Ex: 9-  44  9/  61  9-  1-  --   X  7/  3- 
	//     9  17  33  40  49  50  50  70  83  86
	//
	public String toString()
	{
		return getScoreboardDisplay();
	}



	private boolean isStrikeOrSpare(Mark mark)
	{
		return ((mark == Mark.STRIKE) || (mark == Mark.SPARE));
	}

	//*************************************************
	//*************************************************
	//*************************************************
	//*********ASSIGNMENT METADATA STUFF***************
	public String getFirstNameOfSubmitter() 
	{
		String firstName = "Amanda";
		return firstName;
	}

	public String getLastNameOfSubmitter() 
	{
		String lastName = "Ortiz";
		return lastName;
	}

	public double getHoursSpentWorkingOnThisAssignment()
	{
		return 16;

	}

	public int getScoreAgainstTestCasesSubset()
	{
		return 125;
	}
}