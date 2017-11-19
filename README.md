# mcc
МосКокс - VJ для МосКокс

Настройка целевой платформы RCP.

Главное меню Eclipse - Windows - Preferences - Plug-in Development - Targets
Надо создать целевую платформу "TS-RCP-4.6-multi-platform" со следующим содержимым:
1. Directory "${workspace_loc}/../git-repos/ts-targets/target-rcp-ts-addons"
2. Directory "${workspace_loc}/../git-repos/ts-targets/target-ts-addons"
3. Software site: "http://download.eclipse.org/eclipse/updates/4.6"

Директории добавляются в целевую платформу как обычно.
А вот удаленныый источник плагинов (Software site) добавляется так:

В окне диплога ("Edit Target Definition") редактирования целевой платыормы надо
нажать "Add", выбрать "Software site", нажать "Next" и в поле "Work with:" ввести
"http://download.eclipse.org/eclipse/updates/4.6". Нажать "Add" и в диалоге
дать любое название сайту, нажать "Ok".

После этого нужно подождать, пока появится список с содерржимым сайта. В появившемся списке выбрать
"Eclipse RCP Target Components" и 
"Equinox Target Components"

Внимание: надо УБРАТЬ  отметку с "Include required software" и ПОСТАВТЬ отметку
"Include all environments", после чего можно нажать "Finish" и закрыть все диалоги.

Целевая платформа создана, не забудьте активировать ее.

