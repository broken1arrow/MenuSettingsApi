# MenuSettingsApi usage.

You need use two classes. More info in the wiki.

## register the api in main class like this.
```
	public class YourMainClass extends JavaPlugin {
  
private MenuCache menuYaml;

		@Override
		public void onEnable() {
			// this = your main class.
      // if you only want make one menu file set this to true, in other case put 
      // in the foldername and it will find all yml inside the folder (it will use the filenames as keys).
		RegisterMenuAddon registerMenuAddon = new RegisterMenuAddon(this, "your file name or foldername here", true);
		menuYaml = registerMenuAddon.getMenuCache();
		menuYaml.reload();
		}

		@Override
		public void onDisable() {
			// own code here
		}
		public static YourMainClass getInstance() {
			return YourMainClass.getPlugin(YourMainClass.class);
		}
    
	public MenuCache getMenuYaml() {
		return menuYaml;
	}
	}
  ```
  
  ## How you use it with my menu api
  Link to mu api https://github.com/broken1arrow/MenuLibrary but should also work with your own menu.
  you create new instance of GetMenuButtonsData() class and get data from there.
  
  ```
  
  public class testCache extends MenuHolder {
	MenuSettings menuData;
	GetMenuButtonsData menuDatahelp;
	
	public testCache(Player player, String menuname) {
		menuDatahelp = new GetMenuButtonsData(menuname, player);
		menuData = menuDatahelp.getMenucache();
		setMenuSize(menuData.getMenuSize());
		setTitle(menuData.getMenuTitle());
		
	}

	@Override
	public MenuButton getButtonAt(int slot) {
		return getMenubutton(slot);
	}

	public MenuButton getMenubutton(int slot) {
		ItemSettings buttonData = menuDatahelp.getButton(slot);
		if (buttonData == null) return null;
		return new MenuButton() {
			@Override
			public void onClickInsideMenu(Player player, Inventory inventory, ClickType clickType, ItemStack itemStack, Object o) {
      
//checkClickRequirements return boolen, so if player not have the Requirements you set in the file it will return false.
				menuDatahelp.checkClickRequirements(buttonData, clickType);
			
			}

                  @Override
			public long updateTime() {
				return menuDatahelp.getRefreshButton();
			}

			@Override
			public boolean updateButton() {
				return buttonData.isUpdateButton();
			}

			@Override
			public ItemStack getItem() {
				return CreateItemStack.of(buttonData.getIcon(), buttonData.getDisplayname(), buttonData.getLore()).makeItemStack();
			}
		};
	}
}
 
```
  
