import re, json, pathlib
from typing import List, Tuple, Dict


name_id_dict : Dict[str, int] = {}
parent_dir = pathlib.Path(__file__).parent.resolve()

def get_file_as_string(filepath: str):
    with open(filepath, "r") as file:
        return file.read()

class Territory:
    territories = []
    def __init__(self, name, path, id, x, y, width, height):
        self.name = name
        self.path = path
        self.id = id
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        name_id_dict[name] = id
        Territory.territories.append(self)
        print("Added Territory", name)

    def __init__(self, name, path, id):
        self.name = name
        self.path = path
        self.id = id
        name_id_dict[name] = id
        Territory.territories.append(self)
        print("Added Territory", name)

    def set_borders(self, borders):
        self.borders = borders

    def get_path(self):
        return self.path
    
    def get_by_id(id):
        return Territory.territories[id]
    
    def __str__(self):
        return self.name + " " + str(self.id)
    
    def to_dict(self):
        # print(json.dumps(self.__dict__))
        return self.__dict__


# name, path, id
def generate_territories(json: str, normalize) -> List[Territory]:
    territories = []    
    general_regex = r"<path id=\"([^\"]*)\" .+?(?=d=\")d=\"([^\"]*)\""
    matches = re.finditer(general_regex, json, re.MULTILINE)
    for matchNum, match in enumerate(matches, start=1):    
        name = match.group(1)
        path_str = match.group(2)

        if normalize:
            normalized_path, path_x, path_y, path_width, path_height = normalize_svg_path(path_str)
            # normalized_path = normalized_path.split(" ")
            territories.append(Territory(name, normalized_path, matchNum - 1, path_x, path_y, path_width, path_height))
        else:
            territories.append(Territory(name, path_str, matchNum - 1))

    return territories

def get_dimensinos(json: str) -> Tuple[int, int]:
    # dimensions
    border_regex = r"width=\"([^\"]*)\" height=\"([^\"]*)\""
    matches = re.search(border_regex, json)

    if matches:    
        return (matches.group(1), matches.group(2))
    
    raise Exception("No Dimensions found")


def normalize_svg_path(path):
    # Split the path into individual commands
    commands = re.findall('[a-zA-Z][^a-zA-Z]*', path)
    
    # Find the starting point of the path and calculate the width of the path
    x_coords = []
    y_coords = []
    for command in commands:
        args = re.findall('[-.\d]+', command)
        for i in range(len(args)):
            if i % 2 == 0:
                x_coords.append(float(args[i]))
            else:
                y_coords.append(float(args[i]))
    x_offset = min(x_coords)
    y_offset = min(y_coords)
    width = round(max(x_coords) - x_offset, 2)
    height = round(max(y_coords) - y_offset, 2)
    
    # Normalize the path by subtracting the offset from all coordinates
    normalized_path = ''
    for command in commands:
        normalized_path += command[0]
        args = re.findall('[-.\d]+', command)
        for i in range(len(args)):
            if i % 2 == 0:
                args[i] = str(round(float(args[i]) - x_offset, 2))
            else:
                args[i] = str(round(float(args[i]) - y_offset, 2))
        normalized_path += ' '.join(args) + ' '
    
    return normalized_path.strip(), x_offset, y_offset, width, height

def get_borders(json_str : str):
    json_array = json.loads(json_str)

    for entry in json_array:
        territory_name = next(iter(entry))
        border_names = entry[territory_name]

        id = name_id_dict[territory_name]
        borders = []
        for border_name in border_names:
            borders.append(name_id_dict[border_name])
        Territory.get_by_id(id).set_borders(borders)

def get_json_obj(map_name: str, normalize):
    jsonInput = get_file_as_string(f"{parent_dir}/svgs/{map_name}.svg")
    territories : List[Territory] = generate_territories(jsonInput, normalize)

    width, height = get_dimensinos(jsonInput)

    border_json = get_file_as_string(f"{parent_dir}/borders/{map_name}.json")
    get_borders(border_json)

    territory_jsons : List[str] = [t.to_dict() for t in territories]
    
    risk_map = {'territories': territory_jsons, 'width': width, 'height': height, 'name': map_name}
    # print(risk_map)
    return json.dumps(risk_map)

if __name__ == "__main__":
    map_name = "classic"
    normalize = False
    my_str = get_json_obj(map_name, normalize)
    
    mode_str = "cutted" if normalize else "normal"
    with open(f"{parent_dir}/jsons/{mode_str}/{map_name}.json", "w") as file:
        file.write(my_str)
        