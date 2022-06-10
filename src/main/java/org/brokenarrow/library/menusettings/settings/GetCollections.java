package org.brokenarrow.library.menusettings.settings;

import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.clickactions.CommandActionType;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.brokenarrow.library.menusettings.utillity.Tuple;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class GetCollections {

	public static List<ClickActionTask> formatCommands(List<String> comands) {
		if (comands == null || comands.isEmpty()) return null;
		List<ClickActionTask> list = new ArrayList<>();
		for (String command : comands) {
			CommandActionType commandType = CommandActionType.getType(command);

			if (commandType != null) {
				command = command.replace(commandType.getIdentifier(), "");
				if (command.startsWith(" "))
					command = command.trim();
				ClickActionTask action = new ClickActionTask(commandType, command);
				action.setChance("-1");
				action.setDelay("-1");
				list.add(action);
			}
		}
		return list;
	}

	public static List<ItemFlag> getItemFlags(List<String> itemFlags) {
		if (itemFlags == null || itemFlags.isEmpty()) return null;
		List<ItemFlag> itemFlagList = new ArrayList<>();
		for (String itemFlag : itemFlags) {
			if (itemFlag == null) continue;
			try {
				itemFlag = itemFlag.toUpperCase();
				ItemFlag flag = ItemFlag.valueOf(itemFlag);
				itemFlagList.add(flag);
			} catch (IllegalArgumentException exception) {
				exception.printStackTrace();
			}
		}
		return itemFlagList;
	}

	public static List<Pattern> getPattern(Map<String, String> enchantmentList) {
		if (enchantmentList == null || enchantmentList.isEmpty()) return null;
		List<Pattern> enchantmentsMap = new ArrayList<>();
		for (Map.Entry<String, String> petterns : enchantmentList.entrySet()) {
			String pattern = petterns.getKey();
			String color = petterns.getValue();

			if (pattern != null) {
				pattern = pattern.toUpperCase();
				if (color != null) {
					color = color.toUpperCase();
					DyeColor dyeColor = DyeColor.valueOf(color);
					enchantmentsMap.add(new Pattern(dyeColor, PatternType.valueOf(pattern)));
				} else
					enchantmentsMap.add(new Pattern(DyeColor.WHITE, PatternType.valueOf(pattern)));
			}
		}
		return enchantmentsMap;
	}

	public static Map<Enchantment, Tuple<Integer, Boolean>> getEnchantments(Map<String, Map<String, String>> enchantmentList) {
		if (enchantmentList == null || enchantmentList.isEmpty()) return null;
		Map<Enchantment, Tuple<Integer, Boolean>> enchantmentsMap = new HashMap<>();
		System.out.println("enchantmentList " + enchantmentList);
		for (Map.Entry<String, Map<String, String>> stringMapEntry : enchantmentList.entrySet()) {
			Valid.checkNotNull(stringMapEntry.getKey(), "Enchantment is null. Should always return a value");
			Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(stringMapEntry.getKey().toLowerCase()));
			System.out.println("Enchantment " + enchantment);

			Map<String, String> enchantOptions = stringMapEntry.getValue();
			String enchantmentLevel = enchantOptions.get("level");
			int level = enchantmentLevel == null ? 1 : Integer.parseInt(enchantmentLevel);
			boolean levelRestriction = Boolean.getBoolean(enchantOptions.get("level_restriction"));

			if (enchantment != null)
				enchantmentsMap.put(enchantment, new Tuple<>(level, !levelRestriction));
		}

		return enchantmentsMap;
	}

	public static List<PotionEffect> getPotionEffects(Map<String, Map<String, String>> portionsEffects) {
		if (portionsEffects == null || portionsEffects.isEmpty()) return null;
		List<PotionEffect> potionEffectList = new ArrayList<>();
		System.out.println("portionsEffects " + portionsEffects);
		for (Map.Entry<String, Map<String, String>> potionEffect : portionsEffects.entrySet()) {
			Valid.checkNotNull(potionEffect.getKey(), "Portion effects is null. Should always return a value");
			PotionEffectType potionEffectType = PotionEffectType.getByName(potionEffect.getKey());

			System.out.println("portionsEffects potionEffectType  " + potionEffectType);

			Map<String, String> potionEffectValue = potionEffect.getValue();
			System.out.println("portionsEffects value  " + potionEffectValue);
			if (potionEffectType != null)
				potionEffectList.add(new PotionEffect(potionEffectType, Integer.parseInt(potionEffectValue.get("duration")), Integer.parseInt(potionEffectValue.get("amplifier"))));
		}
		return potionEffectList;
	}

	public static List<Integer> getSlot(String slots) {
		List<Integer> slotList = new ArrayList<>();

		if (slots == null || slots.equals(""))
			return new ArrayList<>();

		try {
			for (String slot : slots.split(",")) {
				if (slot.equals("")) {
					continue;
				}
				if (slot.contains("-")) {
					int firstSlot = Integer.parseInt(slot.split("-")[0]);
					int lastSlot = Integer.parseInt(slot.split("-")[1]);
					slotList.addAll(IntStream.rangeClosed(firstSlot, lastSlot).boxed().collect(Collectors.toList()));
				} else
					slotList.add(Integer.valueOf(slot));

			}
		} catch (NumberFormatException e) {
			throw new NumberFormatException("can not parse this " + slots + " as numbers.");
		}
		return slotList;
	}

	public static Map<List<Integer>, List<ButtonSettings>> sortList(Map<List<Integer>, List<ButtonSettings>> buttonsCache) {
		Map<List<Integer>, List<ButtonSettings>> sortedButtons = new HashMap<>();
		for (Map.Entry<List<Integer>, List<ButtonSettings>> entry : buttonsCache.entrySet()) {
			List<ButtonSettings> value = entry.getValue();
			value = value.stream().sorted(Comparator.comparingInt(ButtonSettings::getPriority)).collect(Collectors.toList());
			sortedButtons.put(entry.getKey(), value);
		}
		return sortedButtons;
	}

}
