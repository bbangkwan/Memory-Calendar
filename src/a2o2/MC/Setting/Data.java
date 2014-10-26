package a2o2.MC.Setting;

/**
 * ������ Ŭ�� ������ ����ϴ� ������ ������ Ŭ����
 * Ÿ��Ʋ, ����, ����(int)�� ������ �����ͷ� �����Ǿ� �ִ�.
 * 
 * @author croute
 * @since 2011.07.12
 */
public class Data
{
	// Ÿ��Ʋ
	private String title;
	
	// ����
	private String description;
	
	// ����
	private int color;

	/**
	 * ������ Ŭ���� �⺻ ������
	 */
	public Data()
	{
		super();
	}

	/**
	 * ������ Ŭ���� ������
	 * 
	 * @param title Ÿ��Ʋ
	 * @param description ����
	 * @param color ����
	 */
	public Data(String title, String description, int color)
	{
		super();
		this.title = title;
		this.description = description;
		this.color = color;
	}

	/**
	 * ���� �̸��� ��ȯ�մϴ�.
	 * 
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * ���� �̸��� �����մϴ�.
	 * 
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * ���� ���� ������ ��ȯ�մϴ�.
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * ���� ���� ������ �����մϴ�.
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * ������ ��ȯ�մϴ�.
	 * 
	 * @return
	 */
	public int getColor()
	{
		return color;
	}

	/**
	 * ������ �����մϴ�.
	 * 
	 * @param color Color.SOMETHING
	 */
	public void setColor(int color)
	{
		this.color = color;
	}

}
