
public class Validator {
	private boolean valid = true;
	private StringBuilder fullComment = new StringBuilder();
	
	public void addComment(String newComment, boolean invalidate) {
		if (fullComment.length() != 0) {
			fullComment.append(System.lineSeparator());
		}
		fullComment.append(newComment);
		valid = false;
	}
	
	public String getComment() {
		return fullComment.toString();
	}
	
	public boolean isValid() {
		return valid;
	}
	
}
