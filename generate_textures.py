"""
Script para gerar texturas placeholder 16x16 para o mod Redstone Plus.
Execute com: python generate_textures.py
Requer: pip install Pillow
"""

from PIL import Image, ImageDraw
import os

# Diretório de texturas
BLOCK_DIR = "src/main/resources/assets/redstone_plus/textures/block"
ITEM_DIR = "src/main/resources/assets/redstone_plus/textures/item"

# Cores para cada bloco (nome: (cor_base, cor_detalhe))
BLOCK_COLORS = {
    "wireless_receiver": ((60, 60, 70), (0, 200, 100)),      # Cinza + Verde
    "memory_block": ((70, 60, 60), (200, 180, 0)),           # Marrom + Amarelo
    "logic_gate": ((60, 50, 70), (180, 0, 180)),             # Roxo escuro + Roxo
    "advanced_repeater": ((80, 80, 80), (200, 50, 50)),      # Cinza + Vermelho
    "player_pressure_plate": ((180, 150, 50), (120, 100, 30)),  # Dourado
    "filtered_hopper": ((100, 100, 100), (50, 50, 50)),      # Cinza + Preto
    "redstone_counter": ((70, 70, 80), (200, 0, 0)),         # Cinza azulado + Vermelho
    "randomizer": ((50, 80, 50), (0, 200, 200)),             # Verde escuro + Ciano
    "programmable_timer": ((80, 70, 60), (255, 100, 0)),     # Marrom + Laranja
    "redstone_storage": ((150, 50, 50), (200, 0, 0)),        # Vermelho escuro + Vermelho
}

def create_block_texture(name, base_color, detail_color):
    """Cria uma textura de bloco 16x16"""
    img = Image.new('RGBA', (16, 16), base_color + (255,))
    draw = ImageDraw.Draw(img)
    
    # Borda
    draw.rectangle([0, 0, 15, 15], outline=(30, 30, 30, 255))
    
    # Detalhes de circuito
    draw.line([(3, 8), (13, 8)], fill=detail_color + (255,), width=1)
    draw.line([(8, 3), (8, 13)], fill=detail_color + (255,), width=1)
    
    # Pontos de destaque
    draw.point((4, 4), fill=detail_color + (255,))
    draw.point((12, 4), fill=detail_color + (255,))
    draw.point((4, 12), fill=detail_color + (255,))
    draw.point((12, 12), fill=detail_color + (255,))
    
    # Centro
    draw.rectangle([6, 6, 9, 9], fill=detail_color + (255,))
    
    return img

def create_analyzer_texture():
    """Cria a textura para o analisador de redstone"""
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Cabo
    draw.line([(8, 4), (8, 14)], fill=(100, 80, 60, 255), width=2)
    
    # Ponta
    draw.polygon([(6, 2), (10, 2), (8, 6)], fill=(200, 50, 50, 255))
    
    # Detalhe de redstone
    draw.point((8, 3), fill=(255, 100, 100, 255))
    
    return img

def main():
    # Criar diretórios se não existirem
    os.makedirs(BLOCK_DIR, exist_ok=True)
    os.makedirs(ITEM_DIR, exist_ok=True)
    
    # Gerar texturas de blocos
    for name, (base, detail) in BLOCK_COLORS.items():
        texture = create_block_texture(name, base, detail)
        texture.save(os.path.join(BLOCK_DIR, f"{name}.png"))
        print(f"Gerado: {name}.png")
    
    # Gerar textura do analisador
    analyzer = create_analyzer_texture()
    analyzer.save(os.path.join(ITEM_DIR, "redstone_analyzer.png"))
    print("Gerado: redstone_analyzer.png")
    
    print("\nTodas as texturas foram geradas!")

if __name__ == "__main__":
    main()
