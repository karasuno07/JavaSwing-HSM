use master
go

drop database if exists quanlycf
go

create database quanlycf
go

use quanlycf
go

create table NhanVien (
	MaNV nvarchar(20) not null primary key,
	TenNV nvarchar(100) not null,
	MatKhau nvarchar(50) not null,
	GioiTinh bit not null,
	DiaChi nvarchar(255),
	SDT nvarchar(20),
	VaiTro bit not null,
	Hinh nvarchar(100)
	)
go

create table KhuyenMai (
	MaKM int identity(1,1) primary key,
	TenKM nvarchar(100) not null,
	NgayBatDau date not null,
	NgayKetThuc date not null,
	ChietKhau nvarchar(50) not null,
	GhiChu nvarchar(255)
	)
go

create table NhomKhachHang (
	MaNhomKH int identity(1,1) primary key,
	TenNhomKH nvarchar(20) not null
	)
go

create table ChiTietKhuyenMai (
	MaNhomKH int not null,
	MaKM int not null,
	constraint pk_CTKM primary key (MaNhomKH, MaKM),
	constraint fk_Chitiet_of_KhuyenMai
	foreign key (MaKM) references KhuyenMai (MaKM) on update cascade on delete cascade,
	constraint fk_Chitiet_of_NhomKhachHang
	foreign key (MaNhomKH) references NhomKhachHang (MaNhomKH) on update cascade on delete cascade
	)
go

create table KhachHang (
	MaKH int identity (1,1) primary key,
	TenKH nvarchar(100) not null,
	GioiTinh bit not null,
	MaNhomKH int not null,
	DiaChi nvarchar(255),
	NgaySinh date,
	Email nvarchar(100),
	SDT nvarchar(20),
	constraint fk_KhachHang_belongsto_NhomKhachHang
	foreign key (MaNhomKH) references NhomKhachHang (MaNhomKH) on update cascade
	)
go

create table HoaDon (
	MaHD char(14) not null primary key,
	MaNV nvarchar(20) not null,
	NgayLapHD datetime not null default getdate(),
	MaKH int,
	PhuThu money,
	ChietKhau money,
	TongTien money not null,
	GhiChu nvarchar(255),
	constraint fk_HoaDon_createby_NhanVien
	foreign key (MaNV) references NhanVien (MaNV) on update cascade,
	constraint fk_HoaDon_of_KhachHang
	foreign key (MaKH) references KhachHang (MaKH) on update cascade
	)
go

create table DanhMuc (
	MaDanhMuc int identity (1,1) primary key,
	TenDanhMuc nvarchar(50) not null
	)
go

create table MatHang (
	MaHang char(10) not null primary key,
	TenHang nvarchar(50) not null,
	MaDanhMuc int not null,
	DonViTinh nvarchar(15) not null,
	NSX nvarchar(50),
	Hinh nvarchar(100),
	constraint fk_MatHang_belongsto_DanhMuc
	foreign key (MaDanhMuc) references DanhMuc (MaDanhMuc) on update cascade
	)
go

create table HoaDonChiTiet (
	MaHDCT int identity(1,1) primary key,
	MaHD char(14) not null,
	MaHang char(10) not null,
	TenHang nvarchar(50) not null,
	SoLuong int not null,
	DonGia money not null,
	ThanhTien money not null,
	constraint fk_ChiTiet_of_HoaDon
	foreign key (MaHD) references HoaDon (MaHD) on update cascade on delete cascade
	)
go

create table KhoHang (
	MaHang char(10) not null primary key,
	SoLuong int not null,
	GiaNhap money not null,
	GiaBan money not null,
	TinhTrangHang nvarchar(50),
	constraint fk_KhoHang_of_MatHang
	foreign key (MaHang) references MatHang (MaHang) on update cascade on delete cascade
	)
go

create table NhaCungCap (
	MaNCC int identity(1,1) primary key,
	TenNCC nvarchar(100) not null,
	SDT nvarchar(20),
	DiaChi nvarchar(255),
	NguoiLienHe nvarchar(50),
	GhiChu nvarchar(255)
	)
go

create table PhieuNhap (
	MaPhieuNhap char(14) primary key,
	MaNV nvarchar(20) not null,
	MaNCC int not null,
	NgayNhap datetime not null,
	TongTien money not null,
	GhiChu nvarchar(255),
	constraint fk_PhieuNhap_by_NhanVien
	foreign key (MaNV) references NhanVien (MaNV) on update cascade,
	constraint fk_NhapHang_by_NhaCungCap
	foreign key (MaNCC) references NhaCungCap (MaNCC) on update cascade
	)
go

create table PhieuNhapChiTiet (
	MaPNCT int identity(1,1) primary key,
	MaPhieuNhap char(14) not null,
	MaHang char(10) not null,
	SoLuongNhap int not null,
	GiaNhap money not null,
	ThanhTien money not null,
	constraint fk_ChiTiet_of_PhieuNhap
	foreign key (MaPhieuNhap) references PhieuNhap (MaPhieuNhap) on update cascade on delete cascade,
	constraint fk_PhieuNhap_of_MatHang
	foreign key (MaHang) references MatHang (MaHang) on update cascade
	)
go

insert into NhanVien(MaNV, TenNV, MatKhau, GioiTinh, VaiTro) values ('admin', 'Administrator', '123456', 1, 1)
GO

insert into DanhMuc(TenDanhMuc) values (N'Chưa xác định')
go

insert into NhomKhachHang(TenNhomKH) values (N'Khách thường')
go

insert into KhachHang(TenKH, GioiTinh, MaNhomKH) values (N'Khách vãng lai', true, 1)
go

-- Không cho phép xóa danh mục mặc định
CREATE TRIGGER DanhMucTrigger
ON DanhMuc
INSTEAD OF DELETE 
AS BEGIN
	SET NOCOUNT ON;

	IF EXISTS(SELECT 1 FROM deleted WHERE MaDanhMuc = 1)
	BEGIN 
		RAISERROR('NOT ALLOW TO DELETE DanhMuc ChuaXacDinh AS IS A SUPER OBJECT', 16, 1)
		ROLLBACK TRANSACTION
		RETURN
	END

	DELETE FROM DanhMuc WHERE MaDanhMuc IN (SELECT MaDanhMuc FROM deleted)
END
GO

-- Cập nhật tình trạng hàng khi thêm hàng mới vào kho
CREATE TRIGGER TinhTrangHangHoa_insert
ON KhoHang AFTER INSERT 
AS BEGIN
	DECLARE @SL INT
	SET @SL = (SELECT SoLuong FROM KhoHang WHERE MaHang IN (SELECT MaHang FROM inserted))
	IF @SL > 0
		BEGIN
			UPDATE KhoHang SET TinhTrangHang = N'Còn hàng' WHERE MaHang IN (SELECT MaHang FROM inserted)
		END
	ELSE
		BEGIN
			UPDATE KhoHang SET TinhTrangHang = N'Hết hàng' WHERE MaHang IN (SELECT MaHang FROM inserted)
		END
END
GO

-- Cập nhật tình trạng hàng khi số lượng hàng thay đổi
CREATE TRIGGER TinhTrangHangHoa_update
ON KhoHang AFTER UPDATE 
AS BEGIN
	DECLARE @SL INT
	SET @SL = (SELECT SoLuong FROM KhoHang WHERE MaHang IN (SELECT MaHang FROM inserted))
	IF @SL > 0
		BEGIN
			UPDATE KhoHang SET TinhTrangHang = N'Còn hàng' WHERE MaHang IN (SELECT MaHang FROM inserted)
		END
	ELSE
		BEGIN
			UPDATE KhoHang SET TinhTrangHang = N'Hết hàng' WHERE MaHang IN (SELECT MaHang FROM inserted)
		END
END
GO

-- Cập nhật lại số lượng hàng trong kho khi thanh toán giỏ hàng
CREATE TRIGGER chkMuaHang ON HoaDonChiTiet
AFTER INSERT 
AS BEGIN
	UPDATE KhoHang
	SET SoLuong = KhoHang.SoLuong - (
		SELECT SoLuong 
		FROM inserted 
		WHERE MaHang = KhoHang.MaHang
	)
	FROM KhoHang JOIN inserted ON KhoHang.MaHang = inserted.MaHang
END
GO

-- Cập nhật lại số lượng hàng trong khi nhập hàng vào kho
CREATE TRIGGER chkNhapHang ON PhieuNhapChiTiet
AFTER INSERT
AS BEGIN
	UPDATE KhoHang
	SET SoLuong = KhoHang.SoLuong + (
		SELECT SoLuongNhap
		FROM inserted
		WHERE MaHang = KhoHang.MaHang
	), KhoHang.GiaNhap = inserted.GiaNhap
	FROM KhoHang JOIN inserted ON KhoHang.MaHang = inserted.MaHang

END
GO

CREATE FUNCTION fn_TongQuanThuChi (@dayGap INT)
RETURNS @table TABLE (Thu MONEY, Chi MONEY)
AS
BEGIN
	DECLARE @Thu MONEY, @Chi MONEY
	SELECT @Thu = SUM(TongTien) FROM HoaDon WHERE (GETDATE() - NgayLapHD) <= @dayGap
	SELECT @Chi = SUM(TongTien) FROM PhieuNhap WHERE (GETDATE() - NgayNhap) <= @dayGap
	INSERT INTO @table VALUES(@Thu, @Chi)
	RETURN
END
GO

CREATE PROCEDURE sp_ThongKeDS_byTime @from DATE, @to DATE
AS
BEGIN
	SELECT HoaDon.MaHD AS DonHang, NgayLapHD AS NgayBan, TenNV, SUM(HoaDonChiTiet.SoLuong) AS SoLuong, TongTien AS DoanhSo
	FROM HoaDon JOIN NhanVien ON HoaDon.MaNV = NhanVien.MaNV
				JOIN HoaDonChiTiet ON HoaDon.MaHD = HoaDonChiTiet.MaHD
	WHERE NgayLapHD BETWEEN @from AND @to
	GROUP BY HoaDon.MaHD, NgayLapHD, TenNV, TongTien
END
GO

CREATE PROCEDURE sp_ThongKeDS_byProduct @from DATE, @to DATE, @MaDM NVARCHAR(10)
AS
BEGIN
	SELECT MatHang.MaHang, MatHang.TenHang, TenDanhMuc AS NhomHang, SUM(TongTien) AS DoanhSo, SUM(HoaDonChiTiet.SoLuong) AS SoLuong
	FROM HoaDonChiTiet 
		JOIN MatHang ON HoaDonChiTiet.MaHang = MatHang.MaHang
		JOIN HoaDon ON HoaDon.MaHD = HoaDonChiTiet.MaHD
		JOIN DanhMuc ON MatHang.MaDanhMuc = DanhMuc.MaDanhMuc
	WHERE (NgayLapHD BETWEEN @from AND @to) AND (DanhMuc.MaDanhMuc LIKE @MaDM)
	GROUP BY MatHang.MaHang, MatHang.TenHang, TenDanhMuc
	ORDER BY MatHang.MaHang
END
GO

CREATE PROCEDURE sp_ThongKeDS_byStaff @from DATE, @to DATE, @MaDM NVARCHAR(10)
AS
BEGIN
	SELECT TenNV, SUM(TongTien) AS DoanhSo, COUNT(HoaDon.MaHD) AS SoDonHang, SUM(HoaDonChiTiet.SoLuong) AS SoLuongHang
	FROM NhanVien
		JOIN HoaDon ON NhanVien.MaNV = HoaDon.MaNV
		JOIN HoaDonChiTiet On HoaDon.MaHD = HoaDonChiTiet.MaHD
		JOIN MatHang ON HoaDonChiTiet.MaHang = MatHang.MaHang
		JOIN DanhMuc on MatHang.MaDanhMuc = DanhMuc.MaDanhMuc
	WHERE (NgayLapHD BETWEEN @from AND @to) AND (DanhMuc.MaDanhMuc LIKE @MaDM)
	GROUP BY TenNV
END
GO

CREATE PROCEDURE sp_ThongKeHH_byMaDM @from DATE, @to DATE, @MaDM NVARCHAR(10)
AS
BEGIN
	SELECT TenDanhMuc, SUM(HoaDonChiTiet.SoLuong) AS SLHangBan, SUM(TongTien) AS DoanhSo
	FROM DanhMuc
		JOIN MatHang ON MatHang.MaDanhMuc = DanhMuc.MaDanhMuc
		JOIN HoaDonChiTiet ON MatHang.MaHang = HoaDonChiTiet.MaHang
		JOIN HoaDon ON HoaDon.MaHD = HoaDonChiTiet.MaHD
	WHERE (DanhMuc.MaDanhMuc LIKE @MaDM) AND (NgayLapHD BETWEEN @from AND @to)
	GROUP BY TenDanhMuc
END
GO


CREATE PROCEDURE sp_ThongKeHH_summary @from DATE, @to DATE, @MaDM NVARCHAR(10)
AS
BEGIN
	SELECT SUM(HoaDonChiTiet.SoLuong) AS TongHangBan , SUM(TongTien) AS TongDoanhSo
	FROM HoaDon JOIN HoaDonChiTiet ON HoaDon.MaHD = HoaDonChiTiet.MaHD
			JOIN MatHang ON MatHang.MaHang = HoaDonChiTiet.MaHang
			JOIN DanhMuc ON DanhMuc.MaDanhMuc = MatHang.MaDanhMuc
	WHERE (NgayLapHD BETWEEN @from AND @to) AND (DanhMuc.MaDanhMuc LIKE @MaDM) 
END
GO

CREATE PROCEDURE sp_ThongKeLN_byTime @from DATE, @to DATE
AS
BEGIN
	SELECT NgayLapHD AS Ngay,
		   SUM(ThanhTien) AS TienHang, 
		   SUM(ChietKhau) AS KMDonHang, 
		   SUM(PhuThu) AS PhuThu,
		   SUM(ThanhTien - ChietKhau + PhuThu) AS DoanhSo,
		   SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienVon, 
		   SUM(ThanhTien - ChietKhau + PhuThu) - SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienLai
	FROM HoaDon JOIN HoaDonChiTiet ON HoaDon.MaHD = HoaDonChiTiet.MaHD
				JOIN KhoHang ON HoaDonChiTiet.MaHang = KhoHang.MaHang
	WHERE NgayLapHD BETWEEN @from AND @to
	GROUP BY NgayLapHD
	ORDER BY NgayLapHD
END
GO

CREATE PROCEDURE sp_ThongKeLN_summary @from DATE, @to DATE, @MaDM NVARCHAR(10)
AS
BEGIN
	SELECT SUM(ThanhTien - ChietKhau + PhuThu) AS DoanhSo,
	       SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienVon, 
	       SUM(ThanhTien - ChietKhau + PhuThu) - SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienLai
	FROM HoaDon JOIN HoaDonChiTiet ON HoaDon.MaHD = HoaDonChiTiet.MaHD
			JOIN KhoHang ON HoaDonChiTiet.MaHang = KhoHang.MaHang
			JOIN MatHang ON HoaDonChiTiet.MaHang = MatHang.MaHang
			JOIN DanhMuc ON DanhMuc.MaDanhMuc = MatHang.MaDanhMuc
	WHERE (NgayLapHD BETWEEN @from AND @to) AND (DanhMuc.MaDanhMuc LIKE @MaDM)
END
GO

CREATE PROCEDURE sp_ThongKeLN_byMaDM @from DATE, @to DATE, @MaDM NVARCHAR(10)
AS
BEGIN
	SELECT MatHang.TenHang, 
	   SUM(ThanhTien) AS TienHang,
	   SUM(ChietKhau) AS KMDonHang, 
	   SUM(ThanhTien - ChietKhau) AS DoanhSo,
	   SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienVon, 
	   SUM(ThanhTien - ChietKhau) - SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienLai
	FROM MatHang 
       JOIN HoaDonChiTiet ON HoaDonChiTiet.MaHang = MatHang.MaHang
	   JOIN KhoHang ON HoaDonChiTiet.MaHang = KhoHang.MaHang
	   JOIN HoaDon ON HoaDonChiTiet.MaHD = HoaDon.MaHD
	   JOIN DanhMuc ON DanhMuc.MaDanhMuc = MatHang.MaDanhMuc
	WHERE (NgayLapHD BETWEEN @from AND @to) AND (DanhMuc.MaDanhMuc LIKE @MaDM)
	GROUP BY MatHang.TenHang
END
GO

CREATE PROCEDURE sp_GetDSPN @MaHang CHAR(10)
AS
BEGIN
	SELECT NgayNhap, PhieuNhap.MaPhieuNhap, SoLuongNhap, GhiChu
	FROM PhieuNhap JOIN PhieuNhapChiTiet ON PhieuNhap.MaPhieuNhap = PhieuNhapChiTiet.MaPhieuNhap
			   JOIN MatHang ON PhieuNhapChiTiet.MaHang = MatHang.MaHang
	WHERE PhieuNhapChiTiet.MaHang = @MaHang
END
GO

CREATE PROCEDURE sp_BCThang @year INT
AS
BEGIN
	SELECT MONTH(NgayLapHD) AS Thang, YEAR(NgayLapHD) AS Nam,
	   SUM(ThanhTien) AS TienHang, 
	   SUM(ChietKhau) AS KMDonHang, 
	   SUM(PhuThu) AS PhuThu,
	   SUM(ThanhTien - ChietKhau + PhuThu) AS DoanhSo,
	   SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienVon, 
	   SUM(ThanhTien - ChietKhau + PhuThu) - SUM(GiaNhap * HoaDonChiTiet.SoLuong) AS TienLai,
	   SUM(HoaDonChiTiet.SoLuong) AS SLHang,
	   COUNT(HoaDon.MaHD) AS SoDonHang
	FROM HoaDon JOIN HoaDonChiTiet ON HoaDon.MaHD = HoaDonChiTiet.MaHD
			JOIN KhoHang ON HoaDonChiTiet.MaHang = KhoHang.MaHang
	WHERE YEAR(NgayLapHD) = @year
	GROUP BY MONTH(NgayLapHD), YEAR(NgayLapHD)
END
GO

CREATE FUNCTION fn_ThongKeThuChi (@from DATE, @to DATE)
RETURNS @table TABLE (NgayThu DATE, NgayChi DATE, Thu MONEY, Chi MONEY)
AS
BEGIN
	DECLARE @TKThu TABLE (NgayThu DATE, Thu MONEY)
	INSERT INTO @TKThu
	SELECT NgayLapHD, SUM(HoaDon.TongTien) AS Thu
	FROM HoaDon
	WHERE NgayLapHD BETWEEN @from AND @to
	GROUP BY NgayLapHD

	DECLARE @TKChi TABLE (NgayChi DATE, Chi MONEY)
	INSERT INTO @TKChi
	SELECT NgayNhap, SUM(PhieuNhap.TongTien) AS Chi
	FROM PhieuNhap
	WHERE NgayNhap BETWEEN @from AND @to
	GROUP BY NgayNhap

	INSERT INTO @table 
	SELECT NgayThu, NgayChi, Thu, Chi
	FROM @TKThu AS TKT FULL JOIN @TKChi AS TKC ON TKT.NgayThu = TKC.NgayChi
	RETURN
END
GO

CREATE FUNCTION fn_ThongKeThuChi_summary (@from DATE, @to DATE)
RETURNS @table TABLE (TongThu MONEY, TongChi MONEY)
AS
BEGIN
	DECLARE @TongThu MONEY, @TongChi MONEY
	SET @TongThu = (SELECT SUM(TongTien) FROM HoaDon WHERE NgayLapHD BETWEEN @from AND @to)
	SET @TongChi = (SELECT SUM(TongTien) FROM PhieuNhap WHERE NgayNhap BETWEEN @from AND @to)

	INSERT INTO @table VALUES (@TongThu, @TongChi)
	RETURN
END
GO

