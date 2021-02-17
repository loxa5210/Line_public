package com.example.demo.line.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImgurVO {

	private String id;
	private String title;
	private String description;
	private String datetime;
	private String type;
	private String name;
	private String link;

	//	{
	//		"id": "cgkqFdY",
	//		"title": "200X200 Pixel",
	//		"description": "test",
	//		"datetime": 1606719277,
	//		"type": "image/png",
	//		"animated": false,
	//		"width": 512,
	//		"height": 512,
	//		"size": 38139,
	//		"views": 0,
	//		"bandwidth": 0,
	//		"vote": null,
	//		"favorite": false,
	//		"nsfw": null,
	//		"section": null,
	//		"account_url": null,
	//		"account_id": 141776495,
	//		"is_ad": false,
	//		"in_most_viral": false,
	//		"has_sound": false,
	//		"tags": [],
	//		"ad_type": 0,
	//		"ad_url": "",
	//		"edited": "0",
	//		"in_gallery": false,
	//		"deletehash": "bIpIVuAhUJCv1N0",
	//		"name": "pixel.gif",
	//		"link": "https://i.imgur.com/cgkqFdY.png"
	//	},
	//		"success": true,
	//		"status": 200
}
